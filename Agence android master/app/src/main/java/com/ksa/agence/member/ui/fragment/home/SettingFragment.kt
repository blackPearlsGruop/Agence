package com.ksa.agence.member.ui.fragment.home

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agence.member.app.AgenceCompanyApp.Companion.pref
import com.ksa.agence.R
import com.ksa.agence.member.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.common.AppLanguage
import com.ksa.agence.member.common.util.Utilities
import com.ksa.agence.databinding.MemberFragmentSettingBinding
import com.ksa.agence.member.viewModels.AuthenticationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : BaseFragment<MemberFragmentSettingBinding>() {

    override fun getLayoutId(): Int = R.layout.member_fragment_setting
    private val viewModel: AuthenticationViewModel by viewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Wallet balance: demo value until the backend endpoint exists
        mViewDataBinding.tvBalanceAmount.text = "24,500"

        // Favourites are a client feature, not needed for a service provider
        mViewDataBinding.tvFavorite.visibility = View.GONE

        // Language chip shows the current language, tapping switches instantly
        updateLanguageChipLabel()

        onClick()
    }

    private fun updateLanguageChipLabel() {
        // Language names are always shown in their own language
        mViewDataBinding.tvLanguageChip.text =
            getString(if (AppLanguage.current(requireContext()) == "ar") R.string.lang_name_ar else R.string.lang_name_en)
    }

    private fun initResponse() {
        // resend response
        viewModel.userLogOutAppResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                pref.clearSharedPref()
                                // Back to the client / service provider picker
                                com.ksa.agence.common.RoleManager.backToRoleSelection(requireActivity())
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


    private fun onClick() {

        val comingSoon = View.OnClickListener {
            Toast.makeText(requireContext(), getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }

        mViewDataBinding.ivNotification.setOnClickListener(comingSoon)

        // Wallet card shortcuts open the member wallet screen
        mViewDataBinding.btnTopUp.setOnClickListener {
            mViewDataBinding.root.findNavController().navigate(R.id.menuWallet)
        }
        mViewDataBinding.btnWithdraw.setOnClickListener {
            mViewDataBinding.root.findNavController().navigate(R.id.menuWallet)
        }
        mViewDataBinding.btnBalanceHistory.setOnClickListener {
            mViewDataBinding.root.findNavController().navigate(R.id.menuWallet)
        }

        // Payment methods: no backend endpoint yet
        mViewDataBinding.tvPaymentMada.setOnClickListener(comingSoon)
        mViewDataBinding.tvPaymentApplePay.setOnClickListener(comingSoon)
        mViewDataBinding.tvPaymentCard.setOnClickListener(comingSoon)
        mViewDataBinding.tvPaymentTabby.setOnClickListener(comingSoon)
        mViewDataBinding.tvPaymentTamara.setOnClickListener(comingSoon)

        // Language: switches immediately for the whole app, no separate page
        mViewDataBinding.tvLanguage.setOnClickListener {
            AppLanguage.toggle(requireContext())
            // Recreate keeps the user on this screen with the new language
            requireActivity().recreate()
        }

        mViewDataBinding.tvMyAccount.setOnClickListener {
            val action = SettingFragmentDirections.actionSettingFragmentToMyProfileFragment()
            mViewDataBinding.root.findNavController().navigate(action)
        }

        mViewDataBinding.tvPreviousInvoices.setOnClickListener(comingSoon)

        mViewDataBinding.tvConnectWithUs.setOnClickListener {
            val action = SettingFragmentDirections.actionSettingFragmentToContacUsFragment()
            mViewDataBinding.root.findNavController().navigate(action)
        }

        mViewDataBinding.tvTermsAndConditions.setOnClickListener {
            val action = SettingFragmentDirections.actionSettingFragmentToTermsOfUseFragment()
            mViewDataBinding.root.findNavController().navigate(action)
        }

        mViewDataBinding.tvLogOut.setOnClickListener {
            showDialogLogOutApp(
                getString(R.string.member_sorry),
                getString(R.string.are_you_sure_to_log_out),
                "LogOut"
            )
        }
    }


    fun showDialogLogOutApp(title: String, body: String, type: String) {
        val dialogLogUotApp = Dialog(requireActivity(), R.style.customDialogTheme)
        dialogLogUotApp!!.setCancelable(false)
        val inflater = requireActivity().layoutInflater
        val v: View = inflater.inflate(R.layout.dialog_login_out, null)
        dialogLogUotApp!!.setContentView(v)

        var tvTitle = dialogLogUotApp.findViewById(R.id.title) as TextView
        var tvBody = dialogLogUotApp.findViewById(R.id.body) as TextView
        var btnYes = dialogLogUotApp.findViewById(R.id.btn_yes) as TextView
        var btnNo = dialogLogUotApp.findViewById(R.id.btn_no) as TextView

        tvTitle.text = title
        tvBody.text = body



        btnYes.setOnClickListener {
            viewModel.userLogOutApp()
        }

        btnNo.setOnClickListener {
            dialogLogUotApp.dismiss()
        }



        dialogLogUotApp!!.show()

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