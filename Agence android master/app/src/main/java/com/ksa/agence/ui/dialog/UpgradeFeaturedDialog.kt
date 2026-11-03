package com.ksa.agence.ui.dialog

import android.os.Bundle
import android.view.View
import com.ksa.agence.R
import com.ksa.agence.base.BaseBottomDialog
import com.ksa.agence.databinding.DialogUpgradeFeaturedBinding
import com.ksa.agence.common.util.Utilities

class UpgradeFeaturedDialog : BaseBottomDialog<DialogUpgradeFeaturedBinding>() {

    override fun getLayoutId(): Int = R.layout.dialog_upgrade_featured

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewDataBinding.btnApplyFeatured.setOnClickListener {
            Utilities.showToastError(requireActivity(), getString(R.string.coming_soon))
            dismiss()
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
    }
}
