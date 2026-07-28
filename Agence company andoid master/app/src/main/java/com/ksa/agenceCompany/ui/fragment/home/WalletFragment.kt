package com.ksa.agenceCompany.ui.fragment.home

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.base.BaseFragment
import com.ksa.agenceCompany.common.CODE200
import com.ksa.agenceCompany.common.CODE422
import com.ksa.agenceCompany.common.Resource
import com.ksa.agenceCompany.common.util.Utilities
import com.ksa.agenceCompany.databinding.FragmentChatBinding
import com.ksa.agenceCompany.databinding.FragmentWalletBinding
import com.ksa.agenceCompany.viewModels.AuthenticationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WalletFragment : BaseFragment<FragmentWalletBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_wallet

    private val authenticationViewModel: AuthenticationViewModel by viewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        onClick()
    }

    private fun onClick() {
        mViewDataBinding.btnBalanceBalance.setOnClickListener {
            val name=mViewDataBinding.tvFullName.text.toString()
            val bankName=mViewDataBinding.tvBankName.text.toString()
            val bankAccountNumber=mViewDataBinding.tvBankAccountNumber.text.toString()
            val ibanNumber=mViewDataBinding.tvIbanNumber.text.toString()

            if (name.isEmpty())
            {
                mViewDataBinding.tvFullName.error=getString(R.string.full_name)
            }
            else  if (bankName.isEmpty())
            {
                mViewDataBinding.tvBankName.error=getString(R.string.bank_name)
            }
            else  if (bankAccountNumber.isEmpty())
            {
                mViewDataBinding.tvBankAccountNumber.error=getString(R.string.bank_account_number)
            }
           else if (ibanNumber.isEmpty())
            {
                mViewDataBinding.tvIbanNumber.error=getString(R.string.iban_number)
            }
            else{
                authenticationViewModel.sendWithdrawalRequest(name,bankName,bankAccountNumber,ibanNumber)
            }
        }
    }


    private fun initResponse() {

        // resend response
        authenticationViewModel.me()
        authenticationViewModel.meResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {

                                mViewDataBinding.tvAvailableBalance.text=""+it.data!!.bank_account!!.data!!.wallet +" "+getString(R.string.r_s)
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
        authenticationViewModel.sendWithdrawalRequestResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                showDialogSuccess()
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



    fun showDialogSuccess() {
        val dialog = Dialog(requireActivity(), R.style.customDialogTheme)
        dialog.setCancelable(false)
        val inflater = requireActivity().layoutInflater
        val v: View = inflater.inflate(R.layout.dialog_success_order, null)
        dialog.setContentView(v)

        val ivClose = dialog.findViewById<ImageView>(R.id.imageViewClose)
        val orderTitle = dialog.findViewById<TextView>(R.id.textViewTitel)
        val orderNo = dialog.findViewById<TextView>(R.id.tv_order_number)

        orderTitle.text = getString(R.string.you_have_been_submitted_to_management)
        orderNo.visibility = View.GONE

        ivClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }


}