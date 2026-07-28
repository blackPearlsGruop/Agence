package com.ksa.agence.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.adapter.AllOrdersAdapter
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentOrdersBinding
import com.ksa.agence.entity.allOrdersResponse.DataAllOrdersResponse
import com.ksa.agence.interfaces.Order
import com.ksa.agence.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrdersFragment : BaseFragment<FragmentOrdersBinding>(), Order {

    override fun getLayoutId(): Int = R.layout.fragment_orders
    private val viewModel: HomeViewModel by viewModel()


    lateinit var allOrdersAdapter: AllOrdersAdapter
    lateinit var listData: ArrayList<DataAllOrdersResponse>



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listData=ArrayList()

        onClick()


    }

    private fun initResponse() {

        // resend response
        val statusArray = listOf("pending", "in-progress")
        viewModel.allOrders(statusArray)
        viewModel.allOrdersResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    listData.clear()
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {

                                listData.addAll(it.data!!)
                                allOrdersAdapter =
                                    AllOrdersAdapter(requireActivity(), listData,this)
                                mViewDataBinding.rvAllOrder.adapter = allOrdersAdapter
                                allOrdersAdapter.notifyDataSetChanged()

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
        mViewDataBinding.btnCurrentRequests.setOnClickListener {
            mViewDataBinding.btnCurrentRequests.setBackgroundResource(R.drawable.shape_bottom)
            mViewDataBinding.tvCurrentRequests.setTextColor(resources.getColor(R.color.white))

            mViewDataBinding.btnFinishedRequests.setBackgroundDrawable(null)
            mViewDataBinding.tvFinishedRequests.setTextColor(resources.getColor(R.color.primary))

            val statusArray = listOf("pending", "in-progress")
            viewModel.allOrders(statusArray)

        }

        mViewDataBinding.btnFinishedRequests.setOnClickListener {
            mViewDataBinding.btnFinishedRequests.setBackgroundResource(R.drawable.shape_bottom)
            mViewDataBinding.tvFinishedRequests.setTextColor(resources.getColor(R.color.white))

            mViewDataBinding.btnCurrentRequests.setBackgroundDrawable(null)
            mViewDataBinding.tvCurrentRequests.setTextColor(resources.getColor(R.color.primary))

            val statusArray = listOf("completed", "canceled")
            viewModel.allOrders(statusArray)

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

    override fun clickItemOrder(idOrder: Int) {

        val action=OrdersFragmentDirections.actionMenuOrdersToShowOrderFragment(idOrder)
        mViewDataBinding.root.findNavController().navigate(action)
    }

    override fun clickItemReorder(idOrder: Int) {

    }


}