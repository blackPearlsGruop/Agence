package com.ksa.agence.ui.fragment.notification

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ksa.agence.R
import com.ksa.agence.adapter.NotificationListAdapter
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.Resource
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentNotifactionBinding
import com.ksa.agence.entity.notificationResponse.DataNotificationResponse
import com.ksa.agence.ui.activity.MainActivity
import com.ksa.agence.viewModels.NotificationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/** Notifications page: all / unread tabs, grouped into Today and Earlier. */
class NotificationFragment : BaseFragment<FragmentNotifactionBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_notifaction

    private val viewModel: NotificationViewModel by viewModel()
    private lateinit var mainActivity: MainActivity

    private lateinit var adapter: NotificationListAdapter
    private val allItems = ArrayList<DataNotificationResponse>()
    private var showUnreadOnly = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()

        adapter = NotificationListAdapter(requireContext())
        mViewDataBinding.rvNotification.adapter = adapter

        mViewDataBinding.btnBack.setOnClickListener { findNavController().popBackStack() }
        mViewDataBinding.btnTabAll.setOnClickListener { selectTab(unreadOnly = false) }
        mViewDataBinding.btnTabUnread.setOnClickListener { selectTab(unreadOnly = true) }

        observeResponse()
        render()
    }

    private fun selectTab(unreadOnly: Boolean) {
        showUnreadOnly = unreadOnly
        val b = mViewDataBinding
        b.btnTabAll.setBackgroundResource(if (unreadOnly) 0 else R.drawable.bg_notif_tab_on)
        b.btnTabAll.setTextColor(if (unreadOnly) 0xFF3D3D3D.toInt() else 0xFFFFFFFF.toInt())
        b.btnTabUnread.setBackgroundResource(if (unreadOnly) R.drawable.bg_notif_tab_on else 0)
        b.tvTabUnread.setTextColor(if (unreadOnly) 0xFFFFFFFF.toInt() else 0xFF3D3D3D.toInt())
        render()
    }

    private fun render() {
        val unreadCount = allItems.count { NotificationListAdapter.isUnread(it) }
        mViewDataBinding.tvUnreadCount.text = unreadCount.toString()
        mViewDataBinding.tvUnreadCount.visibility = if (unreadCount > 0) View.VISIBLE else View.GONE

        val shown = if (showUnreadOnly) allItems.filter { NotificationListAdapter.isUnread(it) } else allItems
        adapter.submit(shown)

        val empty = shown.isEmpty()
        mViewDataBinding.layoutEmpty.visibility = if (empty) View.VISIBLE else View.GONE
        mViewDataBinding.rvNotification.visibility = if (empty) View.GONE else View.VISIBLE
        mViewDataBinding.tvEmptyTitle.setText(
            if (showUnreadOnly) R.string.notif_empty_unread_title else R.string.notif_empty_title
        )
        mViewDataBinding.tvEmptyDesc.setText(
            if (showUnreadOnly) R.string.notif_empty_unread_desc else R.string.notif_empty_desc
        )
    }

    private fun observeResponse() {
        viewModel.notificationResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let {
                        if (it.code == CODE200) {
                            allItems.clear()
                            allItems.addAll(it.data.orEmpty())
                            render()
                        } else {
                            Utilities.showToastError(requireActivity(), it.message.orEmpty())
                        }
                    }
                }

                is Resource.Error -> {
                    showProgress(false)
                    Log.i("Notifications", "error")
                }

                is Resource.Loading -> showProgress(true)
            }
        })
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) viewModel.getNotification()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mainActivity.isInitialized) mainActivity.showHomeToolbar()
    }
}
