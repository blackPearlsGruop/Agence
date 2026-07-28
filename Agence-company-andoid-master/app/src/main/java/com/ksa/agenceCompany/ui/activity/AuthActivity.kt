package com.ksa.agenceCompany.ui.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.base.BaseActivity
import com.ksa.agenceCompany.databinding.ActivityAuthBinding

class AuthActivity : BaseActivity<ActivityAuthBinding>() {

    override fun getLayoutId(): Int =R.layout.activity_auth


    private lateinit var navHostFragment: NavHostFragment
    // lateinit var type: String
    var navController: NavController? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_auth) as NavHostFragment
        navController = navHostFragment.navController

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



}