package com.ksa.agence.ui.fragment.splash

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.databinding.FragmentChoosePageBinding
import com.ksa.agence.ui.activity.AuthActivity
import com.ksa.agence.ui.activity.MainActivity

class ChoosePageFragment : BaseFragment<FragmentChoosePageBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_choose_page

    private val SPLASH_TIME = 2000L


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        onClick()
    }


    fun onClick() {

        mViewDataBinding.btnCustomer.setOnClickListener {
            val action = ChoosePageFragmentDirections.actionChoosePageFragment2ToLoginFragment()
            mViewDataBinding.root.findNavController().navigate(action)
        }

        mViewDataBinding.btnProvider.setOnClickListener {
            val action = ChoosePageFragmentDirections.actionChoosePageFragment2ToLoginFragment()
            mViewDataBinding.root.findNavController().navigate(action)
        }

        mViewDataBinding.btnGuest.setOnClickListener {

            openActivityAndFinish(MainActivity::class.java)

        }


    }


    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        // يتم استدعاء هذه الدالة عندما يتغير حالة الاتصال
        if (isConnected) {
            // يمكنك إجراء أي إجراءات إضافية هنا عند الاتصال بالإنترنت

        } else {
        }

    }


}