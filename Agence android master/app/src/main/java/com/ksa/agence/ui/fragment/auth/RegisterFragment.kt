package com.ksa.agence.ui.fragment.auth

import android.graphics.Paint
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
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
import com.ksa.agence.databinding.FragmentRegisterBinding
import com.ksa.agence.viewModels.AuthenticationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_register
    private lateinit var nameUser: String
    private lateinit var phone: String
    private val viewModel: AuthenticationViewModel by viewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewDataBinding.titleIAgreeTo.paintFlags =
            mViewDataBinding.titleIAgreeTo.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        onClick()
    }


    private fun initResponse() {
        // resend response
        viewModel.registerResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                Utilities.showToastSuccess(requireActivity(), it.message!!)
                                val action =
                                    RegisterFragmentDirections.actionRegisterFragmentToConfirmOtpFragment(
                                        phone,"+966")
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



    fun onClick() {

        mViewDataBinding.btnRegisterNow.setOnClickListener {

            nameUser=mViewDataBinding.tvFullName.text.toString()
            phone=mViewDataBinding.tvMobileNumber.text.toString()

            if (nameUser.isEmpty())
            {
                mViewDataBinding.tvFullName.error=getString(R.string.this_item_is_required)
            }
            else if (phone.isEmpty())
            {
                mViewDataBinding.tvMobileNumber.error=getString(R.string.this_item_is_required)
            }
            else if (mViewDataBinding.checkBoxIAgreeTo.isChecked==false)
            {
                Utilities.showToastError(requireActivity(),getString(R.string.terms_and_conditions) )
            }
            else{
                phone="+966"+phone
                viewModel.userRegister(nameUser,phone,1)
            }


        }

        mViewDataBinding.tvSignIn.setOnClickListener {
            val action = RegisterFragmentDirections
                .actionRegisterFragmentToLoginFragment()
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