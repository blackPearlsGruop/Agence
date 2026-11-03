package com.ksa.agence.member.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.member.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.member.common.util.Utilities
import com.ksa.agence.databinding.FragmentOpportunityDetailsBinding
import com.ksa.agence.member.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class OpportunityDetailsFragment : BaseFragment<FragmentOpportunityDetailsBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_opportunity_details
    private lateinit var flag: String
    private var idOpportunity: Int = 0
    private val viewModel: HomeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            val args: OpportunityDetailsFragmentArgs =
                OpportunityDetailsFragmentArgs.fromBundle(requireArguments())
            idOpportunity = args.idOp
            flag = args.flag
        }

        mViewDataBinding.ivBack.setOnClickListener {
            mViewDataBinding.root.findNavController().popBackStack()
        }

        mViewDataBinding.btnSendToAll.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.msg_applied), Toast.LENGTH_SHORT).show()
        }
    }

    private fun initResponse() {
        viewModel.getSingleOpportunities(idOpportunity)
        viewModel.getSingleOpportunitiesResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                Utilities.onLoadImageFromUrl(requireActivity(), it.data.logo, mViewDataBinding.ivLogo)
                                mViewDataBinding.tvName.text = it.data.company_name
                                mViewDataBinding.tvTitle.text = it.data.title
                                mViewDataBinding.tvDescription.text = it.data.description
                                mViewDataBinding.tvEndDate.text = it.data.end_at
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
                    showProgress(false)
                    Log.i("TestVerification", "error")
                }

                is Resource.Loading -> {
                    Log.i("TestVerification", "loading")
                    showProgress(true)
                }
            }
        })
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            initResponse()
        }
    }
}