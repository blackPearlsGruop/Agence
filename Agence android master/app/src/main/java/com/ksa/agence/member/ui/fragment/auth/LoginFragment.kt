package com.ksa.agence.member.ui.fragment.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.common.AppLanguage
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.databinding.MemberFragmentLoginBinding
import com.ksa.agence.member.base.BaseFragment
import com.ksa.agence.member.common.util.Utilities
import com.ksa.agence.member.viewModels.AuthenticationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : BaseFragment<MemberFragmentLoginBinding>() {

    override fun getLayoutId(): Int = R.layout.member_fragment_login
    private lateinit var phone: String
    private val viewModel: AuthenticationViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.tvLangCode.text = if (AppLanguage.current(requireContext()) == "ar") "AR" else "EN"
        onClick()
    }


    private fun initResponse() {
        viewModel.loginResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                Utilities.showToastSuccess(requireActivity(), it.message!!)
                                val action =
                                    LoginFragmentDirections.actionLoginFragmentToConfirmOtpFragment(
                                        phone, "+966"
                                    )
                                mViewDataBinding.root.findNavController().navigate(action)
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

        // Guest: open the member home directly
        mViewDataBinding.btnGuest.setOnClickListener {
            openActivityAndFinish(com.ksa.agence.member.ui.activity.MainActivity::class.java)
        }

        // Language switch (applies to the whole app, client and member)
        mViewDataBinding.btnToggleLang.setOnClickListener {
            AppLanguage.toggle(requireContext())
            requireActivity().recreate()
        }

        mViewDataBinding.tvForgotPassword.setOnClickListener {
            // TODO: wire once a password-reset flow exists
        }

        // Hidden until Google sign-in is connected to the server
        mViewDataBinding.btnGoogle.visibility = View.GONE
        mViewDataBinding.btnGoogle.setOnClickListener {
            // TODO: integrate Google Sign-In
        }
    }


    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            initResponse()
        }
    }
}
