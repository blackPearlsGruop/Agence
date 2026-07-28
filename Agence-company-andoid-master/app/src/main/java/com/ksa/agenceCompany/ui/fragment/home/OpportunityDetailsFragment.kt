package com.ksa.agenceCompany.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.base.BaseFragment
import com.ksa.agenceCompany.common.CODE200
import com.ksa.agenceCompany.common.CODE422
import com.ksa.agenceCompany.common.Resource
import com.ksa.agenceCompany.common.util.Utilities
import com.ksa.agenceCompany.databinding.FragmentOpportunityDetailsBinding
import com.ksa.agenceCompany.ui.activity.MainActivity
import com.ksa.agenceCompany.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class OpportunityDetailsFragment : BaseFragment<FragmentOpportunityDetailsBinding>()  {

    override fun getLayoutId(): Int = R.layout.fragment_opportunity_details
    private lateinit var flag: String
    private var idOpportunity: Int=0
    private val viewModel: HomeViewModel by viewModel()
    private lateinit var mainActivity: MainActivity



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.opportunity_details)



        if (arguments != null){

            val args:OpportunityDetailsFragmentArgs=OpportunityDetailsFragmentArgs.fromBundle(requireArguments())
            idOpportunity=args.idOp
            flag=args.flag

            onClick()

        }



    }




    private fun initResponse() {

        // resend response
        viewModel.getSingleOpportunities(idOpportunity)
        viewModel.getSingleOpportunitiesResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {

                                Utilities.onLoadImageFromUrl(requireActivity(),it.data.logo,mViewDataBinding.ivLogo)
                                mViewDataBinding.tvName.text=it.data.company_name
                                mViewDataBinding.tvTitle.text=it.data.title
                                mViewDataBinding.tvDescription.text=it.data.description
                                mViewDataBinding.tvEndDate.text=it.data.end_at
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


    private fun onClick() {

        mViewDataBinding.btnSendToAll.setOnClickListener {
            val action=OpportunityDetailsFragmentDirections.actionOpportunityDetailsFragmentToBottomSheetSendValueFragment(idOpportunity)
            mViewDataBinding.root.findNavController().navigate(action)
        }

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
        if (flag=="Home")
        {
            mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.home)

        }
        else{
            mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.opportunities)

        }

    }



}