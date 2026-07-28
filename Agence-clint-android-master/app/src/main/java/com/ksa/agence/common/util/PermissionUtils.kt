package com.ksa.nafhaseha.common.util

import androidx.annotation.RequiresApi
import android.os.Build
import android.app.Activity
import android.content.Context

object PermissionUtils {
    @RequiresApi(api = Build.VERSION_CODES.M)
    fun neverAskAgainSelected(activity: Activity, permission: String?): Boolean {
        val prevShouldShowStatus = getRatinaleDisplayStatus(activity, permission)
        val currShouldShowStatus = activity.shouldShowRequestPermissionRationale(
            permission!!
        )
        return prevShouldShowStatus != currShouldShowStatus
    }

    fun setShouldShowStatus(context: Context, permission: String?) {
        val genPrefs = context.getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE)
        val editor = genPrefs.edit()
        editor.putBoolean(permission, true)
        editor.commit()
    }

    fun getRatinaleDisplayStatus(context: Context, permission: String?): Boolean {
        val genPrefs = context.getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE)
        return genPrefs.getBoolean(permission, false)
    }

//    fun displayNeverAskAgainDialog(activity: Activity, txt: String?) {
//        val builder = AlertDialog.Builder(activity)
//        builder.setMessage(activity.resources.getString(R.string.weNeadTo))
//        builder.setCancelable(false)
//        builder.setPositiveButton(activity.resources.getString(R.string.permitManually)) { dialog, which ->
//            dialog.dismiss()
//            val intent = Intent()
//            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//            val uri = Uri.fromParts("package", activity.packageName, null)
//            intent.data = uri
//            activity.startActivity(intent)
//        }
//        builder.setNegativeButton(activity.resources.getString(R.string.cancel), null)
//        builder.show()
//    }
}