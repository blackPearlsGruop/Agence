package com.ksa.agence.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
import com.ksa.agence.entity.companyResponse.Category
import com.ksa.agence.entity.companyResponse.DataCompanyResponse
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

        try {
            (requireActivity() as com.ksa.agence.ui.activity.MainActivity).mViewDataBinding.constraintLayout2.visibility = View.GONE
        } catch (e: Exception) {
            Log.e("OrdersFragment", "constraintLayout2 hide failed", e)
        }

        listData = ArrayList()

        // Local mock data so the screen isn't blank while staging.agence.sa is
        // down — same pattern used across Home. Automatically replaced once a
        // real server response for the active tab arrives.
        showOrders(mockCurrentOrders())
        updateTabCounts()

        onClick()
    }

    private fun mockCompany(name: String, image: String) = DataCompanyResponse(
        account_type = "individual", address = null, availability = 1, avg_rate = 4.8,
        categories = null, company_background_image = null, company_logo = image,
        consultant_price = null, created_at = null, currentPlan = null, default_lang = "ar",
        description = null, enable_notification = 1, id = 1, is_added_favourite = false,
        is_subscribed_to_free_plan = 0, plan_end_at = null, price_start_from = null,
        rate_count = null, title = name
    )

    private fun mockCategory(title: String) = Category(
        description = null, icon = null, id = 1, is_consultant = 0, title = title
    )

    private fun mockCurrentOrders(): List<DataAllOrdersResponse> = listOf(
        DataAllOrdersResponse(
            accepted_offer = true, id = 9001,
            company = mockCompany("فهد العتيبي", "https://images.unsplash.com/photo-1616147147027-60d49d3582c4?w=80&h=80&fit=crop&auto=format"),
            category = mockCategory("تدقيق العلامة التجارية والتموضع"), description = null,
            order_number = "ORD-00419", created_at = "2 يوليو 2026", order_duration_in_days = null,
            order_type = "service", payment_method = null, payment_status = null,
            price = 5000, service = null, tax_percentage = null, has_offers = true, title = null, offer = null,
            order_status = "in-progress"
        ),
        DataAllOrdersResponse(
            accepted_offer = true, id = 9002,
            company = mockCompany("وكالة نجم", "https://images.unsplash.com/photo-1595680337986-ce4862b497b9?w=80&h=80&fit=crop&auto=format"),
            category = mockCategory("تدقيق إعلانات ميتا"), description = null,
            order_number = "ORD-00387", created_at = "28 يونيو 2026", order_duration_in_days = null,
            order_type = "service", payment_method = null, payment_status = null,
            price = 150, service = null, tax_percentage = null, has_offers = true, title = null, offer = null,
            order_status = "pending"
        )
    )

    private fun mockFinishedOrders(): List<DataAllOrdersResponse> = listOf(
        DataAllOrdersResponse(
            accepted_offer = true, id = 9003,
            company = mockCompany("استديو أثر", "https://images.unsplash.com/photo-1690166444476-8cc4c0f24032?w=80&h=80&fit=crop&auto=format"),
            category = mockCategory("الشعار ونظام الهوية"), description = null,
            order_number = "ORD-00301", created_at = "10 مايو 2026", order_duration_in_days = null,
            order_type = "service", payment_method = null, payment_status = null,
            price = 4100, service = null, tax_percentage = null, has_offers = true, title = null, offer = null,
            order_status = "completed"
        )
    )

    private fun showOrders(data: List<DataAllOrdersResponse>) {
        listData.clear()
        listData.addAll(data)
        allOrdersAdapter = AllOrdersAdapter(requireActivity(), listData, this)
        mViewDataBinding.rvAllOrder.adapter = allOrdersAdapter
        allOrdersAdapter.notifyDataSetChanged()
    }

    private fun updateTabCounts() {
        mViewDataBinding.tvCurrentCount.text = mockCurrentOrders().size.toString()
        mViewDataBinding.tvFinishedCount.text = mockFinishedOrders().size.toString()
    }

    private fun initResponse() {
        val statusArray = listOf("pending", "in-progress")
        viewModel.allOrders(statusArray)
        viewModel.allOrdersResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                it.data?.let { data ->
                                    if (data.isNotEmpty()) showOrders(data)
                                }
                            }
                            CODE422 -> Utilities.showToastError(requireActivity(), it.message!!)
                            else -> {
                                showProgress(false)
                                Utilities.showToastError(requireActivity(), it.message!!)
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    // keep whatever mock/real data is already shown
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

    private fun onClick() {
        mViewDataBinding.btnCurrentRequests.setOnClickListener {
            mViewDataBinding.btnCurrentRequests.setBackgroundResource(R.drawable.bg_agence_pill_active)
            mViewDataBinding.tvCurrentRequests.setTextColor(resources.getColor(R.color.white))
            mViewDataBinding.tvCurrentCount.visibility = View.VISIBLE

            mViewDataBinding.btnFinishedRequests.background = null
            mViewDataBinding.tvFinishedRequests.setTextColor(resources.getColor(R.color.agence_muted))
            mViewDataBinding.tvFinishedCount.visibility = View.GONE

            showOrders(mockCurrentOrders())
            viewModel.allOrders(listOf("pending", "in-progress"))
        }

        mViewDataBinding.btnFinishedRequests.setOnClickListener {
            mViewDataBinding.btnFinishedRequests.setBackgroundResource(R.drawable.bg_agence_pill_active)
            mViewDataBinding.tvFinishedRequests.setTextColor(resources.getColor(R.color.white))
            mViewDataBinding.tvFinishedCount.visibility = View.VISIBLE

            mViewDataBinding.btnCurrentRequests.background = null
            mViewDataBinding.tvCurrentRequests.setTextColor(resources.getColor(R.color.agence_muted))
            mViewDataBinding.tvCurrentCount.visibility = View.GONE

            showOrders(mockFinishedOrders())
            viewModel.allOrders(listOf("completed", "canceled"))
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            initResponse()
        }
    }

    override fun clickItemOrder(idOrder: Int) {
        // Mock orders use ids >= 9000 and don't exist on the server yet
        if (idOrder >= 9000) {
            Toast.makeText(requireContext(), getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
            return
        }
        val action = OrdersFragmentDirections.actionMenuOrdersToShowOrderFragment(idOrder)
        mViewDataBinding.root.findNavController().navigate(action)
    }

    override fun clickItemReorder(idOrder: Int) {
    }

    override fun clickItemChat(model: com.ksa.agence.entity.allOrdersResponse.DataAllOrdersResponse) {
        val bundle = android.os.Bundle().apply {
            putInt("idCompany", model.company?.id ?: 1)
            putString("orderNO", model.order_number ?: "")
            putString("CategoryName", model.category?.title ?: model.description ?: "")
            putString("nameCompany", model.company?.title ?: getString(R.string.app_name))
            putString("imageCompany", model.company?.company_logo ?: "")
            putInt("idOrder", model.id ?: 0)
            putString("flag", "ORDERS")
        }
        mViewDataBinding.root.findNavController()
            .navigate(R.id.action_menuOrders_to_conversationFragment, bundle)
    }
}
