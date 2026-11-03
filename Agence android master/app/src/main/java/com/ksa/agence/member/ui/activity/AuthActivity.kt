package com.ksa.agence.member.ui.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ksa.agence.R
import com.ksa.agence.member.base.BaseActivity
import com.ksa.agence.databinding.MemberActivityAuthBinding

class AuthActivity : BaseActivity<MemberActivityAuthBinding>() {

    override fun getLayoutId(): Int =R.layout.member_activity_auth


    private lateinit var navHostFragment: NavHostFragment
    // lateinit var type: String
    var navController: NavController? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_auth) as NavHostFragment
        navController = navHostFragment.navController

        // Opened from the client home banner: go straight to service provider registration
        if (savedInstanceState == null && intent.getStringExtra("start_at") == "register") {
            navController?.navigate(R.id.registerFragment)
        }
    }

    fun enableEdgeToEdge() {
        val window = window
        val decorView = window.decorView

        // إخفاء شريط الحالة والحفاظ على شريط التنقل
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // تعيين لون شريط الحالة
        window.statusBarColor = Color.TRANSPARENT

        // تعيين لون شريط التنقل
        window.navigationBarColor = resources.getColor(R.color.black)

        // تغيير لون الأيقونات في شريط الحالة إلى الأسود
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            decorView.systemUiVisibility = decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val host = supportFragmentManager.findFragmentById(R.id.nav_host_auth) as? NavHostFragment
        if (host?.navController?.popBackStack() == true) return
        // First login screen: go back to the client / service provider picker
        com.ksa.agence.common.RoleManager.backToRoleSelection(this)
    }
}
