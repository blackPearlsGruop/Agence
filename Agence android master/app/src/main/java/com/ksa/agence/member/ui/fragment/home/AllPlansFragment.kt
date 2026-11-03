package com.ksa.agence.member.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.member.adapter.AllPlansAdapter
import com.ksa.agence.member.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.member.common.util.Utilities
import com.ksa.agence.databinding.FragmentAllPlansBinding
import com.ksa.agence.member.entity.allSubscriptionResponse.DataAllSubscriptionResponse
import com.ksa.agence.member.interfaces.Home
import com.ksa.agence.member.ui.activity.MainActivity
import com.ksa.agence.member.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllPlansFragment : BaseFragment<FragmentAllPlansBinding>(), Home {

    override fun getLayoutId(): Int = R.layout.fragment_all_plans
    private lateinit var urlPay: String
    private lateinit var mainActivity: MainActivity

    private val viewModel: HomeViewModel by viewModel()
    lateinit var allPlansAdapter: AllPlansAdapter
    lateinit var listPlans: ArrayList<DataAllSubscriptionResponse>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.member_subscriptions)


        listPlans = ArrayList()


    }

    private fun initResponse() {

        // resend response
        viewModel.getAllPlan()
        viewModel.planResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                listPlans.addAll(it.data!!)
                                allPlansAdapter =
                                    AllPlansAdapter(requireActivity(), listPlans, this)
                                mViewDataBinding.rvAllPlans.adapter = allPlansAdapter
                                allPlansAdapter.notifyDataSetChanged()

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


        // resend response
        viewModel.subscribeToPlanResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                urlPay = it.data!!.url!!
                                // openLink(requireActivity(),urlPay!!)
                                try {

                                    val action =
                                        AllPlansFragmentDirections.actionAllPlansFragmentToWepViewPaymentFragment(
                                            urlPay
                                        )
                                    mViewDataBinding.root.findNavController().navigate(action)

                                }catch (e:Exception){}


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
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.home)

    }

    override fun clickItemShowService(idService: Int) {

    }

    override fun clickItemOpportunitiesDetails(idOpportunities: Int) {
        viewModel.subscribeToPlan(idOpportunities, "online-payment")
    }

    override fun clickItemUpdateService(idService: Int) {

    }

    override fun clickItemDeleteService(idService: Int, position: Int) {
    }

}