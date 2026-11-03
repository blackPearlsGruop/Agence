package com.ksa.agence.common

import android.app.Activity
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.ksa.agence.app.AgenceApp
import com.ksa.agence.member.app.AgenceCompanyApp
import com.ksa.agence.ui.activity.RoleSelectionActivity

/**
 * Developer shortcut, shown ONLY in debug builds (the app installed from Android Studio).
 * It never appears in the release build that goes to the store.
 *
 * A small DEV tab on the left edge of the home screens lets the developer:
 *  1. switch between client and member without logging out
 *  2. log out of both sides and go back to the login flow
 */
object DevTools {

    private const val TAG_VIEW = "agence_dev_button"

    private fun isDebuggable(activity: Activity): Boolean =
        (activity.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

    fun attach(activity: Activity) {
        if (!isDebuggable(activity)) return
        val content = activity.findViewById<FrameLayout>(android.R.id.content) ?: return
        if (content.findViewWithTag<TextView>(TAG_VIEW) != null) return

        val density = activity.resources.displayMetrics.density
        fun dp(v: Int) = (v * density).toInt()

        val button = TextView(activity).apply {
            tag = TAG_VIEW
            text = "DEV"
            setTextColor(Color.WHITE)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f)
            gravity = Gravity.CENTER
            setPadding(dp(8), dp(6), dp(8), dp(6))
            alpha = 0.85f
            contentDescription = "Developer options"
            background = GradientDrawable().apply {
                setColor(Color.parseColor("#E96D07"))
                cornerRadii = floatArrayOf(0f, 0f, dp(10).toFloat(), dp(10).toFloat(),
                    dp(10).toFloat(), dp(10).toFloat(), 0f, 0f)
            }
            setOnClickListener { showMenu(activity) }
        }

        // Absolute LEFT (not START) so it stays on the same edge in Arabic and English
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.LEFT or Gravity.CENTER_VERTICAL
        )
        content.addView(button, params)
    }

    private fun showMenu(activity: Activity) {
        val options = arrayOf(
            "تبديل نوع الحساب (بدون تسجيل خروج)",
            "تسجيل خروج من الجهتين والرجوع لتسجيل الدخول"
        )
        AlertDialog.Builder(activity)
            .setTitle("أدوات المطور")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> RoleManager.backToRoleSelection(activity)
                    1 -> logOutEverywhere(activity)
                }
            }
            .setNegativeButton("إلغاء", null)
            .show()
    }

    /** Local logout only: clears the saved sessions on this device, the language stays as it is. */
    private fun logOutEverywhere(activity: Activity) {
        AgenceApp.pref.authToken = ""
        AgenceApp.pref.userData = ""
        AgenceCompanyApp.pref.authToken = ""
        AgenceCompanyApp.pref.userData = ""
        RoleManager.clearRole(activity)

        val intent = Intent(activity, RoleSelectionActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        activity.startActivity(intent)
        activity.finish()
    }
}
