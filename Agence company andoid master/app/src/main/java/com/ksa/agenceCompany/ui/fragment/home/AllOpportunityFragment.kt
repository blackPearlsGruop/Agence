package com.ksa.agenceCompany.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.adapter.AllOpportunitiesAdapter
import com.ksa.agenceCompany.base.BaseFragment
import com.ksa.agenceCompany.common.CODE200
import com.ksa.agenceCompany.common.CODE422
import com.ksa.agenceCompany.common.Resource
import com.ksa.agenceCompany.common.util.Utilities
import com.ksa.agenceCompany.databinding.FragmentAllOpportunityBinding
import com.ksa.agenceCompany.entity.allOpportunitiesResponse.DataAllOpportunitiesResponse
import com.ksa.agenceCompany.interfaces.Home
import com.ksa.agenceCompany.ui.activity.MainActivity
import com.ksa.agenceCompany.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllOpportunityFragment : BaseFragment<FragmentAllOpportunityBinding>(), Home {

    override fun getLayoutId(): Int = R.layout.fragment_all_opportunity
    private var id_company: Int?=0
    private var id_order: Int=0
    private var position: Int=0
    private val viewModel: HomeViewModel by viewModel()
    private lateinit var mainActivity: MainActivity



    lateinit var allOpportunitiesAdapter: AllOpportunitiesAdapter
    lateinit var listDataAllOpportunities: ArrayList<DataAllOpportunitiesResponse>




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        mainActivity = requireActivity() as MainActivity
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.opportunities)



    }




    private fun initResponse() {

        listDataAllOpportunities=ArrayList()
        // resend response
        viewModel.allOpportunities()
        viewModel.allOpportunitiesResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    listDataAllOpportunities.clear()
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                listDataAllOpportunities.addAll(it.data!!)
                                allOpportunitiesAdapter =
                                    AllOpportunitiesAdapter(requireActivity(), listDataAllOpportunities,this)
                                mViewDataBinding.rvAnOpportunity.adapter = allOpportunitiesAdapter
                                allOpportunitiesAdapter.notifyDataSetChanged()

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

    override fun onDestroy() {
        super.onDestroy()
        mainActivity.showHomeToolbar()
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.home)

    }

    override fun clickItemShowService(idService: Int) {

    }

    override fun clickItemOpportunitiesDetails(idOpportunities: Int) {

        val action=AllOpportunityFragmentDirections.actionAllOpportunityFragmentToOpportunityDetailsFragment(idOpportunities,"ALL")
        mViewDataBinding.root.findNavController().navigate(action)
    }

    override fun clickItemUpdateService(idService: Int) {

    }

    override fun clickItemDeleteService(idService: Int, position: Int) {
    }


}