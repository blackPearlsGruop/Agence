package com.ksa.agence.ui.fragment.setting

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.ksa.agence.R
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentContacUsBinding
import com.ksa.agence.ui.activity.MainActivity
import com.ksa.agence.viewModels.InfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContactUsFragment : BaseFragment<FragmentContacUsBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_contac_us
    private val viewModel: InfoViewModel by viewModel()


    private lateinit var mainActivity: MainActivity


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.connect_with_us)


        onClick()
    }

    private fun onClick() {

        mViewDataBinding.btnSend.setOnClickListener {

            val titleMessage=mViewDataBinding.tvTitle.text.toString()
            val dicMessage=mViewDataBinding.tvMessageContent.text.toString()

            if (titleMessage.isEmpty())
            {
                mViewDataBinding.tvTitle.error=getString(R.string.this_item_is_required)
            }
            else     if (dicMessage.isEmpty())
            {
                mViewDataBinding.tvMessageContent.error=getString(R.string.this_item_is_required)
            }
            else{
                viewModel.contactUs(titleMessage,dicMessage)

            }

        }
    }


    private fun initResponse() {
        // resend response
        viewModel.contactUsResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                Utilities.showToastSuccess(requireActivity(), it.message!!)
                                mainActivity.navController!!.popBackStack()
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
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.setting)

    }

}