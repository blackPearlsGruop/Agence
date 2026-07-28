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
import com.ksa.agence.app.AgenceApp.Companion.pref
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.common.USER_DATA
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentSettingBinding
import com.ksa.agence.ui.activity.AuthActivity
import com.ksa.agence.viewModels.AuthenticationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : BaseFragment<FragmentSettingBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_setting
    private val viewModel: AuthenticationViewModel by viewModel()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        if (pref.authToken !=null)
        {
            mViewDataBinding.tvUserName.text =
                pref.loadUserData(requireActivity(), USER_DATA)!!.data!!.user!!.name
            Utilities.onLoadImageFromUrl(
                requireActivity(),
                pref.loadUserData(requireActivity(), USER_DATA)!!.data!!.user!!.profile_image,
                mViewDataBinding.ivUserLogin,)

        }

        onClick()


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

        viewModel.userDeleteAccountResponse.observe(viewLifecycleOwner, Observer { result ->
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

        mViewDataBinding.tvMyAccount.setOnClickListener {

//            val action=SettingFragmentDirections.a()
//            mViewDataBinding.root.findNavController().navigate(action)

        }

        mViewDataBinding.tvLanguage.setOnClickListener {

            val action=SettingFragmentDirections.actionSettingFragmentToChooseLanguageFragment()
            mViewDataBinding.root.findNavController().navigate(action)

        }

        mViewDataBinding.tvFavorite.setOnClickListener {

//            val action=SettingFragmentDirections.actionSettingFragmentToChooseLanguageFragment()
//            mViewDataBinding.root.findNavController().navigate(action)

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
        mViewDataBinding.tvDeleteAccount.setOnClickListener {
            showDialogLogOutApp(getString(R.string.sorry),getString(R.string.be_careful_if_you_delete_the_account_you_will_lose_all_data),"DELETE_ACCOUNT")


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
            if (type=="LogOut")
            {
                viewModel.userLogOutApp()

            }
            else{
                viewModel.userDeleteAccount()

            }
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