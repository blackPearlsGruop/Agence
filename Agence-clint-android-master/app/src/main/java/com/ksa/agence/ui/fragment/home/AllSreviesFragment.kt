package com.ksa.agence.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.ksa.agence.entity.categoriesResponse.DataCategoriesResponse
import com.ksa.agence.interfaces.Company
import com.ksa.agence.ui.activity.MainActivity
import com.ksa.agence.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllSreviesFragment : BaseFragment<FragmentAllServiesBinding>(),Company {

    override fun getLayoutId(): Int = R.layout.fragment_all_servies

    private val viewModel: HomeViewModel by viewModel()
    private lateinit var mainActivity: MainActivity


    lateinit var allCategoriesAdapter:AllCategoriesAdapter
    lateinit var listData: ArrayList<DataCategoriesResponse>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.services)


        listData=ArrayList()

    }


    private fun initResponse() {

        // resend response
        viewModel.getCategory()
        viewModel.categoriesResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {

                                listData.addAll(it.data!!)
                                allCategoriesAdapter =
                                    AllCategoriesAdapter(requireActivity(), listData,this)
                                mViewDataBinding.rvAllCategory.adapter = allCategoriesAdapter
                                allCategoriesAdapter.notifyDataSetChanged()

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

    override fun clickItemCompany(idCompany: Int, flag: String) {
    }

    override fun clickItemAddCompanyFav(idCompany: Int, pos: Int) {
    }

    override fun clickItemShowService(idService: Int) {
        val action=AllSreviesFragmentDirections.actionAllSreviesFragmentToShowSreviesFragment(idService,"showService")
        mViewDataBinding.root.findNavController().navigate(action)

    }



}