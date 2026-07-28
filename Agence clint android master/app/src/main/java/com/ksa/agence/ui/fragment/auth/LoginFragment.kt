package com.ksa.agence.ui.fragment.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentLoginBinding
import com.ksa.agence.viewModels.AuthenticationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_login
    private lateinit var phone: String
    private val viewModel: AuthenticationViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        onClick()

    }


    private fun initResponse() {
// resend response
        viewModel.loginResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { response ->
                        when (response.code) {
                            CODE200 -> {
                                Utilities.showToastSuccess(requireActivity(), response.message!!)
                                val action =
                                    LoginFragmentDirections.actionLoginFragmentToConfirmOtpFragment(
                                        phone, "+966"
                                    )
                                mViewDataBinding.root.findNavController().navigate(action)
                            }

                            CODE422 -> {
                                // فحص نوع `data` قبل محاولة الوصول إليه
                                Utilities.showToastError(requireActivity(), response.message!!)
                            }

                            else -> {
                                Utilities.showToastError(requireActivity(), response.message!!)
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


    fun onClick() {

        mViewDataBinding.btnSignIn.setOnClickListener {

            phone = mViewDataBinding.tvMobileNumber.text.toString()

            if (phone.isEmpty()) {
                mViewDataBinding.tvMobileNumber.error = getString(R.string.this_item_is_required)
            } else {
                viewModel.userLogin("+966$phone")
            }

        }

        mViewDataBinding.tvRegisterNow.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
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


}