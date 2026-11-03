package com.ksa.agence.ui.fragment.setting

import android.os.Bundle
import android.view.View
import com.ksa.agence.R
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.databinding.FragmentBillsBinding
import com.ksa.agence.databinding.FragmentChooseLanguageBinding
import com.ksa.agence.ui.activity.MainActivity

class BillsFragment : BaseFragment<FragmentBillsBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_bills

    private lateinit var mainActivity: MainActivity


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.previous_invoices)



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