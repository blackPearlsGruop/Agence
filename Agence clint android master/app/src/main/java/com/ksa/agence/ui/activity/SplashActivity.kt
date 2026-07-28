package com.ksa.agence.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ksa.agence.R
import com.ksa.agence.app.AgenceApp
import com.ksa.agence.base.BaseActivity
import com.ksa.agence.common.FIRST_TIME
import com.ksa.agence.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_splash



    private lateinit var navHostFragment: NavHostFragment

    // lateinit var type: String
    var navController: NavController? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
          enableEdgeToEdge()
      //  makeStatusbarTransparent()
        handler()

    }
    private fun handler() {
        Handler(Looper.myLooper()!!).postDelayed({
            val authToken = AgenceApp.pref.authToken
            Log.i("TestLoginToken", "tokenn $authToken")

            if (authToken.isNullOrEmpty()) {
                Log.i("TestLoginToken", "tokenn is null or empty")
                openActivityAndFinish(AuthActivity::class.java)
            } else {
                Log.i("TestLoginToken", "tokenn is not null or empty")
                openActivityAndFinish(MainActivity::class.java)
            }
            finish()
        }, 2000)
    }

    private fun makeStatusbarTransparent() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        //  window.statusBarColor = resources.getColor(R.color.purple_200)
        // window.navigationBarColor = resources.getColor(R.color.appColorTran)
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        // window.navigationBarColor = resources.getColor(R.color.appColorTran)

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