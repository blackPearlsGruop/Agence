package com.ksa.agence.ui.fragment.notification

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.adapter.CopanyAdapter
import com.ksa.agence.adapter.NotificationAdapter
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentAllServiesBinding
import com.ksa.agence.databinding.FragmentChatBinding
import com.ksa.agence.databinding.FragmentChoosePageBinding
import com.ksa.agence.databinding.FragmentHomeBinding
import com.ksa.agence.databinding.FragmentNotifactionBinding
import com.ksa.agence.databinding.FragmentOffersBinding
import com.ksa.agence.entity.notificationResponse.DataNotificationResponse
import com.ksa.agence.ui.activity.MainActivity
import com.ksa.agence.viewModels.AuthenticationViewModel
import com.ksa.agence.viewModels.NotificationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationFragment : BaseFragment<FragmentNotifactionBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_notifaction
    private lateinit var mainActivity: MainActivity

    private val viewModel: NotificationViewModel by viewModel()


    lateinit var notificationAdapter: NotificationAdapter
    lateinit var listData: ArrayList<DataNotificationResponse>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.notification)



    }


    private fun initResponse() {

        // resend response
        viewModel.getNotification()
        viewModel.notificationResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {

                                listData= ArrayList()
                                listData.addAll(it.data!!)
                                notificationAdapter =
                                    NotificationAdapter(requireActivity(), listData)
                                mViewDataBinding.rvNotification.adapter = notificationAdapter
                                notificationAdapter.notifyDataSetChanged()

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
    }

}