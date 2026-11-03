package com.ksa.agence.ui.fragment.auth

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.databinding.FragmentProviderSuccessBinding
import com.ksa.agence.ui.activity.MainActivity

class ProviderSuccessFragment : BaseFragment<FragmentProviderSuccessBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_provider_success

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClick()
    }

    private fun onClick() {
        // Continue button - navigate to main app or home
        mViewDataBinding.btnContinue.setOnClickListener {
            // Open MainActivity and finish auth activity
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