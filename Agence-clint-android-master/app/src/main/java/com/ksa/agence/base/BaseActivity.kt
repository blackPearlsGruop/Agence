package com.ksa.agence.base

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ksa.agence.R

import com.ksa.agence.app.AgenceApp
import com.ksa.agence.common.LANG
import com.ksa.agence.common.sharedprefrence.PreferencesUtils
import com.ksa.nafhaseha.common.util.LocaleUtil
import java.util.Locale

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    lateinit var mViewDataBinding: T
    protected lateinit var activity: AppCompatActivity
    private lateinit var progressDialog: Dialog
    private lateinit var dialogNoInternet: Dialog


    protected val sharedPreference: PreferencesUtils by lazy {
        (application as AgenceApp).sharedPreference
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        activity = this
        performDataBinding()
        makeStatusbarTransparent()

    }


    private fun performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mViewDataBinding.lifecycleOwner = this
        mViewDataBinding.executePendingBindings()
    }

    /**
     * @return layout resource id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    fun openActivity(destinationActivity: Class<*>, bundle: Bundle? = null) {
        val mainIntent = Intent(activity, destinationActivity)
        bundle?.let { mainIntent.putExtras(it) }
        startActivity(mainIntent)
    }

    fun openActivityAndFinish(destinationActivity: Class<*>, bundle: Bundle? = null) {
        val mainIntent = Intent(activity, destinationActivity)
        bundle?.let { mainIntent.putExtras(it) }
        startActivity(mainIntent)
        finish()
    }

    override fun attachBaseContext(newBase: Context) {
        if (Locale.getDefault().language == "ar") {
            var prefrence = PreferencesUtils(newBase)
            applyOverrideConfiguration(
                LocaleUtil.getLocalizedConfiguration(
                    prefrence.getString(
                        LANG,
                        "ar"
                    )!!
                )
            )
            super.attachBaseContext(newBase)
        } else {
            var prefrence = PreferencesUtils(newBase)
            applyOverrideConfiguration(
                LocaleUtil.getLocalizedConfiguration(
                    prefrence.getString(
                        LANG,
                        "en"
                    )!!
                )
            )
            super.attachBaseContext(newBase)
        }


    }


    fun showProgress(it: Boolean) {
        if (it) showProgressDialog()
        else hideProgressDialog()

    }

    fun showProgressDialog() {
        hideProgressDialog()
        val alertDialogBuilder = AlertDialog.Builder(this).setCancelable(false)
        alertDialogBuilder.setView(R.layout.loader)
        progressDialog = alertDialogBuilder.create()
        progressDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        progressDialog.show()
    }

    fun hideProgressDialog() {
        if (::progressDialog.isInitialized && progressDialog.isShowing) progressDialog.dismiss()
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /*   fun setLocal(lang: String, activity: Activity) {
           Utilities.cacheString(activity, LANG, lang)
           Utilities.setLocal(lang, activity)
       }
   */

    /*   fun showToast() {
           var layoutInflater = LayoutInflater.from(this)
           val layout = layoutInflater.inflate(
               R.layout.full_color_toast, findViewById(R.id.color_toast_view)
           )
   //        layout.color_toast_text.text = title
           layout.color_toast_description.text = getString(R.string.data_used_in_other_phone)
           startPulseAnimation(this, layout = layout)

           // init toast
           val toast = Toast(applicationContext)
           startTimer(20000L, toast)

           // Setting Toast Gravity
           toast.setGravity(60, 50, 250)

           // Setting layout to toast
           toast.view = layout
           toast.show()
       }


       fun warningToast( message: String) {
           var layoutInflater = LayoutInflater.from(this)
           val layout = layoutInflater.inflate(
               R.layout.warning_toast, findViewById(R.id.color_toast_view)
           )
   //        layout.color_toast_text.text = title
           layout.color_toast_description.text = message
           startPulseAnimation(this, layout = layout)

           // init toast
           val toast = Toast(applicationContext)
           startTimer(20000L, toast)

           // Setting Toast Gravity
           toast.setGravity(60, 50, 250)

           // Setting layout to toast
           toast.view = layout
           toast.show()
       }


       fun errorToast(message: String) {
           var layoutInflater = LayoutInflater.from(this)
           val layout = layoutInflater.inflate(
               R.layout.full_color_toast, findViewById(R.id.color_toast_view)
           )
   //        layout.color_toast_text.text = title
           layout.color_toast_description.text = message
           startPulseAnimation(this, layout = layout)
           // init toast
           val toast = Toast(applicationContext)
   //        startTimer(20000L, toast)
           toast.duration = Toast.LENGTH_LONG
           // Setting Toast Gravity
           toast.setGravity(60, 50, 250)

           // Setting layout to toast
           toast.view = layout
           toast.show()
       }


       fun successToast(message: String) {
           var layoutInflater = LayoutInflater.from(this)
           val layout = layoutInflater.inflate(
               R.layout.full_color_toast2, findViewById(R.id.color_toast_view)
           )
   //        layout.color_toast_text.text = title
           layout.color_toast_description.text = message
           startPulseAnimation(this, layout = layout)
           // init toast
           val toast = Toast(applicationContext)
           startTimer(20000L, toast)

           // Setting Toast Gravity
           toast.setGravity(60, 50, 250)

           // Setting layout to toast
           toast.view = layout
           toast.show()
       }
   */
    override fun onResume() {
        super.onResume()
        // Utilities.setLocalFrom(Utilities.getCachedString(this, LANG, "ar"), this)
    }

    fun setLocal(lang: String, activity: Activity) {
//        Utilities.cacheString(activity, LANG, lang)
//        Utilities.setLocal(lang, activity)
    }



    private fun setupWindowAnimations() {
        val fade: Transition? =
            TransitionInflater.from(this).inflateTransition(R.transition.activity_fade)
        window.enterTransition = fade
    }


//    override fun onDestroy() {
//        super.onDestroy()
//            SocketRepository.onDisconnect()
//    }
//    override fun onPause() {
//        super.onPause()
//        SocketRepository.onDisconnect()
//
//    }


    private fun makeStatusbarTransparent() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
          window.statusBarColor = resources.getColor(R.color.white)
//         window.navigationBarColor = resources.getColor(R.color.appColorTran)
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        // window.navigationBarColor = resources.getColor(R.color.appColorTran)

    }
}
