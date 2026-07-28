package com.ksa.agence.ui.fragment.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.app.AgenceApp
import com.ksa.agence.app.AgenceApp.Companion.pref
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.LANG
import com.ksa.agence.databinding.FragmentAllServiesBinding
import com.ksa.agence.databinding.FragmentChatBinding
import com.ksa.agence.databinding.FragmentChooseLanguageBinding
import com.ksa.agence.databinding.FragmentChoosePageBinding
import com.ksa.agence.databinding.FragmentHomeBinding
import com.ksa.agence.databinding.FragmentNotifactionBinding
import com.ksa.agence.databinding.FragmentOffersBinding
import com.ksa.agence.ui.activity.MainActivity

class ChooseLanguageFragment : BaseFragment<FragmentChooseLanguageBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_choose_language

    private lateinit var mainActivity: MainActivity
    var language = "ar"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.the_language)


        if (AgenceApp.pref.getString(LANG, "") == "ar") {
            mViewDataBinding.linearLayoutAr.setBackgroundResource(R.drawable.shape_text)
            mViewDataBinding.linearLayoutEn.setBackgroundDrawable(null)

        } else if (AgenceApp.pref.getString(LANG, "") == "en") {
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