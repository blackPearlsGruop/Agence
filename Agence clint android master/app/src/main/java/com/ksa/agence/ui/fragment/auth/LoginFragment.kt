package com.ksa.agence.ui.fragment.auth

import android.app.AlertDialog
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
import com.ksa.agence.common.LANG
import com.ksa.agence.common.sharedprefrence.PreferencesUtils


class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_login
    private lateinit var phone: String
    private val viewModel: AuthenticationViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            onClick()
        } catch (e: Throwable) {
            showDebugCrash("onViewCreated/onClick setup", e)
        }
    }

    private fun showDebugCrash(where: String, e: Throwable) {
        Log.e("DEBUG_CRASH", "Crash at: $where", e)
        try {
            if (!isAdded || activity == null) return
            AlertDialog.Builder(requireContext())
                .setTitle("Debug: Crash in $where")
                .setMessage("${e.javaClass.name}: ${e.message}\n\n${e.stackTrace.take(8).joinToString("\n")}")
                .setPositiveButton("OK", null)
                .setCancelable(false)
                .show()
        } catch (inner: Throwable) {
            Log.e("DEBUG_CRASH", "Dialog itself failed", inner)
        }
    }


    private fun initResponse() {
        viewModel.loginResponse.observe(viewLifecycleOwner, Observer { result ->
            // ── Wrapped: this runs asynchronously when the server responds,
            // outside the click handler's own try/catch, so it needs its own.
            try {
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

                    else -> {}
                }
            } catch (e: Throwable) {
                showProgress(false)
                showDebugCrash("initResponse observer (server response handling)", e)
            }
        })
    }


    fun onClick() {

        mViewDataBinding.btnSignIn.setOnClickListener {
            try {
                phone = mViewDataBinding.tvMobileNumber.text.toString()

                if (phone.isEmpty()) {
                    mViewDataBinding.tvMobileNumber.error = getString(R.string.this_item_is_required)
                } else {
                    viewModel.userLogin("+966$phone")
                }
            } catch (e: Throwable) {
                showDebugCrash("btnSignIn click", e)
            }
        }

        mViewDataBinding.tvRegisterNow.setOnClickListener {
            try {
                val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                mViewDataBinding.root.findNavController().navigate(action)
            } catch (e: Throwable) {
                showDebugCrash("tvRegisterNow click", e)
            }
        }

        mViewDataBinding.btnGuest.setOnClickListener {
            try {
                val action = LoginFragmentDirections.actionLoginFragmentToChoosePageFragment2()
                mViewDataBinding.root.findNavController().navigate(action)
            } catch (e: Throwable) {
                showDebugCrash("btnGuest click", e)
            }
        }

        mViewDataBinding.btnToggleLang.setOnClickListener {
            val prefrence = PreferencesUtils(requireContext())
            val current = prefrence.getString(LANG, "ar")
            val newLang = if (current == "ar") "en" else "ar"
            prefrence.putString(LANG, newLang)
            requireActivity().recreate()
        }

        mViewDataBinding.tvForgotPassword.setOnClickListener {
            // TODO: wire once a password-reset flow exists
        }

        mViewDataBinding.btnGoogle.setOnClickListener {
            // TODO: integrate Google Sign-In
        }
    }


    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        try {
            if (isConnected) {
                initResponse()
            }
        } catch (e: Throwable) {
            showDebugCrash("onNetworkConnectionChanged", e)
        }
    }


}
