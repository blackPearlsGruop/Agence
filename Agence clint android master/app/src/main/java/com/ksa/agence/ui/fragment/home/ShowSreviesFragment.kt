package com.ksa.agence.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.adapter.AllCategoriesAdapter
import com.ksa.agence.adapter.CopanyAdapter
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentAllServiesBinding
import com.ksa.agence.databinding.FragmentChatBinding
import com.ksa.agence.databinding.FragmentChoosePageBinding
import com.ksa.agence.databinding.FragmentHomeBinding
import com.ksa.agence.databinding.FragmentOffersBinding
import com.ksa.agence.databinding.FragmentShowServiesBinding
import com.ksa.agence.entity.categoriesResponse.DataCategoriesResponse
import com.ksa.agence.entity.companyResponse.DataCompanyResponse
import com.ksa.agence.interfaces.Company
import com.ksa.agence.ui.activity.MainActivity
import com.ksa.agence.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShowSreviesFragment : BaseFragment<FragmentShowServiesBinding>(), Company {

    override fun getLayoutId(): Int = R.layout.fragment_show_servies
    private lateinit var flagPage: String
    private lateinit var mainActivity: MainActivity


    private var idServise: Int=0
    private var position: Int=0
    private val viewModel: HomeViewModel by viewModel()

    lateinit var companyAdapter: CopanyAdapter
    lateinit var  listCompany: ArrayList<DataCompanyResponse>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.services)

        listCompany=ArrayList()

        if (arguments != null){

            val args:ShowSreviesFragmentArgs=ShowSreviesFragmentArgs.fromBundle(requireArguments())
            idServise=args.idServise
            viewModel.categoriesById(idServise)
            flagPage=args.flage


        }

    }


    private fun initResponse() {

        // resend response
        viewModel.categoriesByIdResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {

                                mViewDataBinding.tvTitleService.text=it.data!!.title
                                listCompany.addAll(it.data!!.companies!!)
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
        val action=ShowSreviesFragmentDirections.actionShowSreviesFragmentToShowCompanyFragment(idCompany,"showService")
        mViewDataBinding.root.findNavController().navigate(action)
    }

    override fun clickItemAddCompanyFav(idCompany: Int, pos: Int) {
        position=pos
        viewModel.addFavourites(idCompany)
    }

    override fun clickItemShowService(idCompany: Int) {

    }


    override fun onDestroy() {
        super.onDestroy()
        mainActivity.showHomeToolbar()

        if (flagPage=="Home")
        {
            mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.home)

        }
        else
        {
            mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.services)

        }

    }

}