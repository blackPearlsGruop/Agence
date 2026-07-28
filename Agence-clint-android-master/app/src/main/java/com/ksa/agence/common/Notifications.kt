package com.ksa.agence.common

import android.Manifest
import android.app.Activity
import android.os.Build
import com.example.awesomedialog.AwesomeDialog
import com.example.awesomedialog.body
import com.example.awesomedialog.onNegative
import com.example.awesomedialog.onPositive
import com.example.awesomedialog.title
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.ksa.agence.R

object Notifications {
    fun Activity.showNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionListener = object : PermissionListener {
                override fun onPermissionGranted() {
                }

                override fun onPermissionDenied(deniedPermissions: List<String?>) {
                    this@showNotificationPermission.confirmStringDialog(
                        title = R.string.notification_message, actionTitle = R.string.confirm, true
                    ) {
                        this@showNotificationPermission.showNotificationPermission()
                    }
                }
            }
            TedPermission.create().setPermissionListener(permissionListener).setPermissions(
                Manifest.permission.POST_NOTIFICATIONS
            ).setDeniedTitle(getString(R.string.notification_message))
                .setDeniedMessage(getString(R.string.reject_permission))
                .setGotoSettingButtonText(getString(R.string.setting)).check()
        }
    }

    fun Activity.confirmStringDialog(
        title: Int,
        actionTitle: Int,
        cancelWithFinishApp: Boolean,
        action: () -> Unit,
    ) {
        createDialog(getString(R.string.alert), getString(title), getString(actionTitle), {
            it.dismiss()
            action()
        }, getString(R.string.cancel), {
            it.dismiss()
            if (cancelWithFinishApp) {
                finish()
            }
        })
    }

    private fun Activity.createDialog(
        title: String,
        msg: String,
        positiveTitle: String,
        positive: (dialog: androidx.appcompat.app.AlertDialog) -> Unit,
        negativeTitle: String? = null,
        negative: (dialog: androidx.appcompat.app.AlertDialog) -> Unit? = { }
    ) {
        val awesomeDialog = AwesomeDialog.build(this)
        awesomeDialog.title(title).body(msg).onPositive(positiveTitle) {
            positive(awesomeDialog)
        }.setCancelable(false)
        if (!negativeTitle.isNullOrEmpty()) {
            awesomeDialog.onNegative(negativeTitle) {
                negative(awesomeDialog)
            }
        }
        awesomeDialog.setCancelable(true)
    }
}