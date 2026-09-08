package com.ksa.agenceCompany.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
import com.ksa.agenceCompany.entity.allOrdersResponse.Category
import com.ksa.agenceCompany.entity.allOrdersResponse.DataAllOrdersResponse
import com.ksa.agenceCompany.entity.allOrdersResponse.UserOrder
import com.ksa.agenceCompany.interfaces.Order
import com.ksa.agenceCompany.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrdersFragment : BaseFragment<FragmentOrdersBinding>(), Order {

    override fun getLayoutId(): Int = R.layout.fragment_orders
    private val viewModel: HomeViewModel by viewModel()


    lateinit var allOrdersAdapter: AllOrdersAdapter
    lateinit var listData: ArrayList<DataAllOrdersResponse>

    // FIGMA REDESIGN: two tabs (active / completed), each covering two real
    // statuses. The API here only accepts one status per call (unlike the
    // client app's list-based endpoint), so each tab fetches its two
    // statuses one after another and merges them into the same list.
    private var statusQueue: MutableList<String> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // FIGMA REDESIGN: fragment_orders.xml has its own light header now.
        // MainActivity's destination-changed listener already hides the old
        // shared blue toolbar for this screen, same as Home/Account/Support/Chat.

        listData = ArrayList()

        // Local mock data so the screen isn't blank while staging.agence.sa
        // is down — automatically replaced by real data once a successful
        // server response for this tab arrives.
        showOrders(mockCurrentOrders())
        updateTabCounts()

        onClick()
    }

    private fun mockUser(name: String, image: String) = UserOrder(
        created_at = null, default_lang = null, device_token = null,
        enable_notification = null, id = 1, name = name,
        notification_count = null, phone = null, profile_image = image, updated_at = null
    )

    private fun mockCategory(title: String) = Category(
        description = null, icon = null, id = 1, is_consultant = 0, title = title
    )

    private fun mockCurrentOrders(): List<DataAllOrdersResponse> = listOf(
        DataAllOrdersResponse(
            id = 9001,
            user = mockUser("فهد العتيبي", "https://images.unsplash.com/photo-1616147147027-60d49d3582c4?w=80&h=80&fit=crop&auto=format"),
            category = mockCategory("تدقيق العلامة التجارية والتموضع"),
            order_number = "ORD-00419", created_at = "2 يوليو 2026",
            description = "تدقيق العلامة التجارية والتموضع",
            price = "5,000 ريال", order_type = "service", order_status = "in-progress", has_offers = false
        ),
        DataAllOrdersResponse(
            id = 9002,
            user = mockUser("وكالة نجم", "https://images.unsplash.com/photo-1595680337986-ce4862b497b9?w=80&h=80&fit=crop&auto=format"),
            category = mockCategory("تدقيق إعلانات ميتا"),
            order_number = "ORD-00387", created_at = "28 يونيو 2026",
            description = "تدقيق إعلانات ميتا",
            price = "150 ريال/hr", order_type = "service", order_status = "pending", has_offers = false
        ),
    )

    private fun mockFinishedOrders(): List<DataAllOrdersResponse> = listOf(
        DataAllOrdersResponse(
            id = 9003,
            user = mockUser("استديو أثر", "https://images.unsplash.com/photo-1690166444476-8cc4c0f24032?w=80&h=80&fit=crop&auto=format"),
            category = mockCategory("الشعار ونظام الهوية"),
            order_number = "ORD-00301", created_at = "10 مايو 2026",
            description = "الشعار ونظام الهوية",
            price = "4,100 ريال", order_type = "service", order_status = "completed", has_offers = false
        ),
    )

    private fun showOrders(data: List<DataAllOrdersResponse>) {
        listData.clear()
        listData.addAll(data)
        allOrdersAdapter = AllOrdersAdapter(requireActivity(), listData, this)
        mViewDataBinding.rvAllOrder.adapter = allOrdersAdapter
        allOrdersAdapter.notifyDataSetChanged()
    }

    private fun updateTabCounts() {
        mViewDataBinding.tvPendingCount.text = mockCurrentOrders().size.toString()
        mViewDataBinding.tvCompletedCount.text = mockFinishedOrders().size.toString()
    }

    private fun fetchStatuses(statuses: List<String>) {
        statusQueue = statuses.toMutableList()
        fetchNextInQueue()
    }

    private fun fetchNextInQueue() {
        val next = statusQueue.removeFirstOrNull() ?: return
        viewModel.allOrders(next)
    }

    private fun initResponse() {
        // resend response
        fetchStatuses(listOf("pending", "in-progress"))
        viewModel.allOrdersResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                it.data?.let { data ->
                                    if (data.isNotEmpty()) {
                                        if (statusQueue.size == 1) {
                                            // first status of this tab's batch — replace the mock data
                                            listData.clear()
                                        }
                                        listData.addAll(data)
                                        allOrdersAdapter = AllOrdersAdapter(requireActivity(), listData, this)
                                        mViewDataBinding.rvAllOrder.adapter = allOrdersAdapter
                                        allOrdersAdapter.notifyDataSetChanged()
                                    }
                                } ?: run {
                                    Log.e("OrdersFragment", "Data is null")
                                }
                                // if this tab still has a second status queued, fetch it next
                                fetchNextInQueue()
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
                    // dismiss loading — keep whatever mock/real data is already shown
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
            mViewDataBinding.btnPending.setBackgroundResource(R.drawable.bg_agence_pill_active)
            mViewDataBinding.tvPending.setTextColor(resources.getColor(R.color.white))
            mViewDataBinding.tvPendingCount.visibility = View.VISIBLE

            mViewDataBinding.btnCompleted.background = null
            mViewDataBinding.tvCompleted.setTextColor(resources.getColor(R.color.agence_muted))
            mViewDataBinding.tvCompletedCount.visibility = View.GONE

            showOrders(mockCurrentOrders())
            fetchStatuses(listOf("pending", "in-progress"))
        }

        mViewDataBinding.btnCompleted.setOnClickListener {
            mViewDataBinding.btnCompleted.setBackgroundResource(R.drawable.bg_agence_pill_active)
            mViewDataBinding.tvCompleted.setTextColor(resources.getColor(R.color.white))
            mViewDataBinding.tvCompletedCount.visibility = View.VISIBLE

            mViewDataBinding.btnPending.background = null
            mViewDataBinding.tvPending.setTextColor(resources.getColor(R.color.agence_muted))
            mViewDataBinding.tvPendingCount.visibility = View.GONE

            showOrders(mockFinishedOrders())
            fetchStatuses(listOf("completed", "canceled"))
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
        // Mock orders use ids >= 9000 and don't exist on the server yet
        if (idOrder >= 9000) {
            Toast.makeText(requireContext(), getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
            return
        }
        val action = OrdersFragmentDirections.actionMenuOrdersToShowOrderFragment(idOrder)
        mViewDataBinding.root.findNavController().navigate(action)
    }

    override fun clickItemSendOffer(idOrder: Int) {

    }

    override fun clickItemRejectOrder(idOrder: Int) {
    }


}
