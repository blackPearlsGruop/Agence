package com.ksa.agence.ui.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import com.ksa.agence.R
import com.ksa.agence.base.BaseActivity
import com.ksa.agence.common.AppLanguage
import com.ksa.agence.common.RoleManager
import com.ksa.agence.databinding.ActivityRoleSelectionBinding

/**
 * First screen of the merged app: the user picks whether they use Agence
 * as a client or as a service provider (member). The choice is remembered,
 * so next launches skip this screen until the user logs out.
 */
class RoleSelectionActivity : BaseActivity<ActivityRoleSelectionBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_role_selection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSystemBars()

        mViewDataBinding.cardRoleClient.setOnClickListener { choose(RoleManager.CLIENT) }
        mViewDataBinding.cardRoleMember.setOnClickListener { choose(RoleManager.MEMBER) }

        mViewDataBinding.btnToggleLang.setOnClickListener {
            // Applies to the whole app (client and member)
            AppLanguage.toggle(this)
            recreate()
        }
    }

    private fun choose(role: String) {
        RoleManager.setRole(this, role)
        RoleManager.openRoleHome(this, role)
        finish()
    }

    private fun setupSystemBars() {
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = resources.getColor(R.color.black)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            decorView.systemUiVisibility =
                decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}
