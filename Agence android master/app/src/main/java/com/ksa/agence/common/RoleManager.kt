package com.ksa.agence.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.ksa.agence.app.AgenceApp
import com.ksa.agence.member.app.AgenceCompanyApp
import com.ksa.agence.ui.activity.RoleSelectionActivity

/**
 * Keeps which side of the merged app the user chose (client or member)
 * and opens the right screens for it.
 *
 * The role lives in its own preferences file so logging out of either side
 * (which clears that side's preferences) does not lose it by accident.
 */
object RoleManager {

    const val CLIENT = "client"
    const val MEMBER = "member"

    private const val PREFS_NAME = "Agence_Role"
    private const val KEY_ROLE = "app_role"

    fun getRole(context: Context): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_ROLE, null)

    fun setRole(context: Context, role: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_ROLE, role).apply()
    }

    fun clearRole(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().remove(KEY_ROLE).apply()
    }

    /** Opens the home screen if the user is logged in on that side, otherwise its login flow. */
    fun openRoleHome(activity: Activity, role: String) {
        val target = if (role == MEMBER) {
            if (AgenceCompanyApp.pref.authToken.isNullOrEmpty())
                com.ksa.agence.member.ui.activity.AuthActivity::class.java
            else
                com.ksa.agence.member.ui.activity.MainActivity::class.java
        } else {
            if (AgenceApp.pref.authToken.isNullOrEmpty())
                com.ksa.agence.ui.activity.AuthActivity::class.java
            else
                com.ksa.agence.ui.activity.MainActivity::class.java
        }
        activity.startActivity(Intent(activity, target))
    }

    /** Used on logout and when backing out of a login screen: forget the role and show the picker. */
    fun backToRoleSelection(activity: Activity) {
        clearRole(activity)
        val intent = Intent(activity, RoleSelectionActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        activity.startActivity(intent)
        activity.finish()
    }
}
