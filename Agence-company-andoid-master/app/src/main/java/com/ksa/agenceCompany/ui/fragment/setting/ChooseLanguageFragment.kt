package com.ksa.agenceCompany.ui.fragment.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.AgenceCompanyApp
import com.ksa.agenceCompany.AgenceCompanyApp.Companion.pref
import com.ksa.agenceCompany.base.BaseFragment
import com.ksa.agenceCompany.common.LANG
import com.ksa.agenceCompany.databinding.FragmentChooseLanguageBinding
import com.ksa.agenceCompany.ui.activity.MainActivity

class ChooseLanguageFragment : BaseFragment<FragmentChooseLanguageBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_choose_language

    private lateinit var mainActivity: MainActivity
    var language = "ar"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.the_language)


        if (AgenceCompanyApp.pref.getString(LANG, "") == "ar") {
            mViewDataBinding.linearLayoutAr.setBackgroundResource(R.drawable.shape_text)
            mViewDataBinding.linearLayoutEn.setBackgroundDrawable(null)

        } else if (AgenceCompanyApp.pref.getString(LANG, "") == "en") {
            mViewDataBinding.linearLayoutEn.setBackgroundResource(R.drawable.shape_text)
            mViewDataBinding.linearLayoutAr.setBackgroundDrawable(null)
        }

        onClick()
    }

    private fun onClick() {
        mViewDataBinding.linearLayoutAr.setOnClickListener {
            mViewDataBinding.linearLayoutAr.setBackgroundResource(R.drawable.shape_text)
            mViewDataBinding.linearLayoutEn.setBackgroundDrawable(null)
            language="ar"
        }

        mViewDataBinding.linearLayoutEn.setOnClickListener {
            mViewDataBinding.linearLayoutEn.setBackgroundResource(R.drawable.shape_text)
            mViewDataBinding.linearLayoutAr.setBackgroundDrawable(null)
            language="en"

        }

        mViewDataBinding.btnSave.setOnClickListener {

            if (language == "ar") {
                // make app language arabic
                pref.putString(LANG, "ar")
                // redirect to activity
            } else {
                // make app language english
               pref.putString(LANG, "en")
            }

            var intent = Intent(requireActivity(), MainActivity::class.java)
            intent.putExtra("type", "SETTING")
            startActivity(intent)
            requireActivity().finish()

        }
    }


    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        // يتم استدعاء هذه الدالة عندما يتغير حالة الاتصال
        if (isConnected) {
            // يمكنك إجراء أي إجراءات إضافية هنا عند الاتصال بالإنترنت

        } else {
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        mainActivity.showHomeToolbar()
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.setting)


    }

}