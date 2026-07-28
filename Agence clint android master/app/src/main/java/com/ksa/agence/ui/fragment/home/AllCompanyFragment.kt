package com.ksa.agence.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.adapter.CopanyAdapter
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentAllCompanyBinding
import com.ksa.agence.entity.companyResponse.CompanyResponse
import com.ksa.agence.entity.companyResponse.DataCompanyResponse
import com.ksa.agence.interfaces.Company
import com.ksa.agence.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllCompanyFragment : BaseFragment<FragmentAllCompanyBinding>(),Company {

    override fun getLayoutId(): Int = R.layout.fragment_all_company

    private var position: Int=0
    private val viewModel: HomeViewModel by viewModel()
    lateinit var companyResponse: CompanyResponse
    lateinit var companyAdapter: CopanyAdapter
    lateinit var  listCompany: ArrayList<DataCompanyResponse>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listCompany= ArrayList()



    }

    private fun initResponse() {

        // resend response
        viewModel.getCompany()
        viewModel.companyResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                companyResponse=result.data

                                listCompany.addAll(it.data!!)
                                companyAdapter =
                                    CopanyAdapter(requireActivity(), listCompany,this)
                                mViewDataBinding.rvAllCompany.adapter = companyAdapter
                                companyAdapter.notifyDataSetChanged()

                            }

                            CODE422 -> {
                                Utilities.showToastError(requireActivity(), it.message!!)
                            }

                            else -> {
                                showProgress(false)
                                Utilities.showToastError(requireActivity(), it.message!!)

                            }
                        }
                    }
                }

                is Resource.Error -> {
                    // dismiss loading
                    showProgress(false)
                    Log.i("TestVerification", "error")

                }

                is Resource.Loading -> {
                    // show loading
                    Log.i("TestVerification", "loading")
                    showProgress(true)

                }
            }
        })


        viewModel.addFavouritesResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                Utilities.showToastSuccess(requireActivity(), it.message!!)


                                if(companyResponse.data!![position].is_added_favourite!!) {
                                    companyResponse.data!![position].is_added_favourite=false
                                }else{
                                    companyResponse.data!![position].is_added_favourite=true

                                }


                            }

                            CODE422 -> {
                                Utilities.showToastError(requireActivity(), it.message!!)
                            }

                            else -> {
                                showProgress(false)
                                Utilities.showToastError(requireActivity(), it.message!!)

                            }
                        }
                    }
                }

                is Resource.Error -> {
                    // dismiss loading
                    showProgress(false)
                    Log.i("TestVerification", "error")

                }

                is Resource.Loading -> {
                    // show loading
                    Log.i("TestVerification", "loading")
                    showProgress(true)

                }
            }
        })



    }


    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        // يتم استدعاء هذه الدالة عندما يتغير حالة الاتصال
        if (isConnected) {
            // يمكنك إجراء أي إجراءات إضافية هنا عند الاتصال بالإنترنت
            initResponse()

        } else {
        }

    }

    override fun clickItemCompany(idCompany: Int,flag:String) {
        val action=AllCompanyFragmentDirections.actionAllComanyFragmentToShowCompanyFragment(idCompany,"AllCompany")
        mViewDataBinding.root.findNavController().navigate(action)
    }

    override fun clickItemAddCompanyFav(idCompany: Int, pos: Int) {
        position=pos
        viewModel.addFavourites(idCompany)
    }

    override fun clickItemShowService(idCompany: Int) {

    }

}