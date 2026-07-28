package com.ksa.agenceCompany.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.adapter.AllOrdersAdapter
import com.ksa.agenceCompany.base.BaseFragment
import com.ksa.agenceCompany.common.CODE200
import com.ksa.agenceCompany.common.CODE422
import com.ksa.agenceCompany.common.Resource
import com.ksa.agenceCompany.common.util.Utilities
import com.ksa.agenceCompany.databinding.FragmentOrdersBinding
import com.ksa.agenceCompany.entity.allOrdersResponse.DataAllOrdersResponse
import com.ksa.agenceCompany.interfaces.Order
import com.ksa.agenceCompany.viewModels.HomeViewModel
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
        viewModel.allOrders("pending")
        viewModel.allOrdersResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    listData.clear()
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                it.data?.let { data ->
                                    listData.addAll(data)
                                    allOrdersAdapter = AllOrdersAdapter(requireActivity(), listData,this)
                                    mViewDataBinding.rvAllOrder.adapter = allOrdersAdapter
                                    allOrdersAdapter.notifyDataSetChanged()
                                } ?: run {
                                    // التعامل مع حالة الـ null هنا، مثلاً، إظهار رسالة خطأ
                                    Log.e("OrdersFragment", "Data is null")
                                    listData.clear()

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




    private fun onClick() {
        mViewDataBinding.btnPending.setOnClickListener {
            mViewDataBinding.btnPending.setBackgroundResource(R.drawable.shape_bottom)
            mViewDataBinding.tvPending.setTextColor(resources.getColor(R.color.white))

            mViewDataBinding.btnInProgress.setBackgroundDrawable(null)
            mViewDataBinding.tvInProgress.setTextColor(resources.getColor(R.color.primary))
            mViewDataBinding.btnCanceled.setBackgroundDrawable(null)
            mViewDataBinding.tvCanceled.setTextColor(resources.getColor(R.color.primary))
            mViewDataBinding.btnCompleted.setBackgroundDrawable(null)
            mViewDataBinding.tvCompleted.setTextColor(resources.getColor(R.color.primary))

            val statusArray = listOf("pending", "in-progress")
            viewModel.allOrders("pending")

        }

        mViewDataBinding.btnInProgress.setOnClickListener {
            mViewDataBinding.btnInProgress.setBackgroundResource(R.drawable.shape_bottom)
            mViewDataBinding.tvInProgress.setTextColor(resources.getColor(R.color.white))

            mViewDataBinding.btnPending.setBackgroundDrawable(null)
            mViewDataBinding.tvPending.setTextColor(resources.getColor(R.color.primary))
            mViewDataBinding.btnCanceled.setBackgroundDrawable(null)
            mViewDataBinding.tvCanceled.setTextColor(resources.getColor(R.color.primary))
            mViewDataBinding.btnCompleted.setBackgroundDrawable(null)
            mViewDataBinding.tvCompleted.setTextColor(resources.getColor(R.color.primary))


            val statusArray = listOf("completed", "canceled")
            viewModel.allOrders("in-progress")

        }

        mViewDataBinding.btnCanceled.setOnClickListener {
            mViewDataBinding.btnCanceled.setBackgroundResource(R.drawable.shape_bottom)
            mViewDataBinding.tvCanceled.setTextColor(resources.getColor(R.color.white))

            mViewDataBinding.btnPending.setBackgroundDrawable(null)
            mViewDataBinding.tvPending.setTextColor(resources.getColor(R.color.primary))
            mViewDataBinding.btnInProgress.setBackgroundDrawable(null)
            mViewDataBinding.tvInProgress.setTextColor(resources.getColor(R.color.primary))
            mViewDataBinding.btnCompleted.setBackgroundDrawable(null)
            mViewDataBinding.tvCompleted.setTextColor(resources.getColor(R.color.primary))


            val statusArray = listOf("completed", "canceled")
            viewModel.allOrders("canceled")

        }

        mViewDataBinding.btnCompleted.setOnClickListener {
            mViewDataBinding.btnCompleted.setBackgroundResource(R.drawable.shape_bottom)
            mViewDataBinding.tvCompleted.setTextColor(resources.getColor(R.color.white))

            mViewDataBinding.btnPending.setBackgroundDrawable(null)
            mViewDataBinding.tvPending.setTextColor(resources.getColor(R.color.primary))
            mViewDataBinding.btnCanceled.setBackgroundDrawable(null)
            mViewDataBinding.tvCanceled.setTextColor(resources.getColor(R.color.primary))
            mViewDataBinding.btnInProgress.setBackgroundDrawable(null)
            mViewDataBinding.tvInProgress.setTextColor(resources.getColor(R.color.primary))


            val statusArray = listOf("completed", "canceled")
            viewModel.allOrders("completed")

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

    override fun clickItemSendOffer(idOrder: Int) {

    }

    override fun clickItemRejectOrder(idOrder: Int) {
    }


}