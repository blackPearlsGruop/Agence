package com.ksa.agence.ui.fragment.home

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.app.AgenceApp
import com.ksa.agence.app.AgenceApp.Companion.pref
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.LANG
import com.ksa.agence.common.Resource
import com.ksa.agence.common.USER_DATA
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentSettingBinding
import com.ksa.agence.ui.activity.AuthActivity
import com.ksa.agence.ui.activity.MainActivity
import com.ksa.agence.viewModels.AuthenticationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : BaseFragment<FragmentSettingBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_setting
    private val viewModel: AuthenticationViewModel by viewModel()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Belt-and-suspenders: hide the old shared blue toolbar here too (same
        // fix as Messages), in case this screen is reached without going through
        // MainActivity's bottom nav click handler (e.g. state restoration).
        try {
            (requireActivity() as MainActivity).mViewDataBinding.constraintLayout2.visibility = View.GONE
        } catch (e: Exception) {
            Log.e("SettingFragment", "constraintLayout2 hide failed", e)
        }

        val userData = pref.loadUserData(requireActivity(), USER_DATA)
        // (profile photo/name now live only inside "الملف الشخصي" — no header
        // avatar or duplicate profile card on this screen anymore)

        // Wallet balance: no backend endpoint yet, showing placeholder until the API exists
        mViewDataBinding.tvBalanceAmount.text = "0"

        // Language chip shows the CURRENT language, tapping switches instantly
        updateLanguageChipLabel()

        onClick()


    }

    private fun updateLanguageChipLabel() {
        val current = AgenceApp.pref.getString(LANG, "ar")
        // Language names aren't translated — always show them in their own language
        mViewDataBinding.tvLanguageChip.text = if (current == "ar") "عربي" else "English"
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
                                val mainIntent = Intent(activity, AuthActivity::class.java)
                                requireActivity().startActivity(mainIntent)
                                requireActivity().finish()
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

        mViewDataBinding.ivNotification.setOnClickListener {
            // No dedicated notifications destination wired up yet from this screen
            Utilities.showToastError(requireActivity(), getString(R.string.coming_soon))
        }

        // Wallet actions: no backend endpoint yet
        mViewDataBinding.btnTopUp.setOnClickListener {
            Utilities.showToastError(requireActivity(), getString(R.string.coming_soon))
        }
        mViewDataBinding.btnWithdraw.setOnClickListener {
            Utilities.showToastError(requireActivity(), getString(R.string.coming_soon))
        }
        mViewDataBinding.btnBalanceHistory.setOnClickListener {
            Utilities.showToastError(requireActivity(), getString(R.string.coming_soon))
        }

        // Payment methods: no backend endpoint yet
        mViewDataBinding.tvPaymentMada.setOnClickListener {
            Utilities.showToastError(requireActivity(), getString(R.string.coming_soon))
        }
        mViewDataBinding.tvPaymentApplePay.setOnClickListener {
            Utilities.showToastError(requireActivity(), getString(R.string.coming_soon))
        }
        mViewDataBinding.tvPaymentCard.setOnClickListener {
            Utilities.showToastError(requireActivity(), getString(R.string.coming_soon))
        }

        // Language: switches immediately, no separate page
        mViewDataBinding.tvLanguage.setOnClickListener {
            val current = AgenceApp.pref.getString(LANG, "ar")
            val newLang = if (current == "ar") "en" else "ar"
            pref.putString(LANG, newLang)

            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.putExtra("type", "SETTING")
            startActivity(intent)
            requireActivity().finish()
        }

        mViewDataBinding.tvPreviousInvoices.setOnClickListener {

            val action=SettingFragmentDirections.actionSettingFragmentToBillsFragment()
            mViewDataBinding.root.findNavController().navigate(action)

        }


        mViewDataBinding.tvConnectWithUs.setOnClickListener {

            val action=SettingFragmentDirections.actionSettingFragmentToContacUsFragment()
            mViewDataBinding.root.findNavController().navigate(action)

        }

        mViewDataBinding.tvFavorite.setOnClickListener {

            val action=SettingFragmentDirections.actionSettingFragmentToFavouritesFragment()
            mViewDataBinding.root.findNavController().navigate(action)

        }

        mViewDataBinding.tvMyAccount.setOnClickListener {

            val action=SettingFragmentDirections.actionSettingFragmentToMyProfileFragment()
            mViewDataBinding.root.findNavController().navigate(action)

        }

        mViewDataBinding.tvTermsAndConditions.setOnClickListener {

            val action=SettingFragmentDirections.actionSettingFragmentToTermsOfUseFragment()
            mViewDataBinding.root.findNavController().navigate(action)

        }

        mViewDataBinding.tvLogOut.setOnClickListener {
            showDialogLogOutApp(getString(R.string.sorry),getString(R.string.are_you_sure_to_log_out),"LogOut")


        }
    }


    fun showDialogLogOutApp(title:String,body:String,type:String) {
        val dialogLogUotApp = Dialog(requireActivity(), R.style.customDialogTheme)
        dialogLogUotApp!!.setCancelable(false)
        val inflater = requireActivity().layoutInflater
        val v: View = inflater.inflate(R.layout.dialog_login_out, null)
        dialogLogUotApp!!.setContentView(v)

        var tvTitle = dialogLogUotApp.findViewById(R.id.title) as TextView
        var tvBody = dialogLogUotApp.findViewById(R.id.body) as TextView
        var btnYes = dialogLogUotApp.findViewById(R.id.btn_yes) as TextView
        var btnNo = dialogLogUotApp.findViewById(R.id.btn_no) as TextView

        tvTitle.text=title
        tvBody.text=body



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
