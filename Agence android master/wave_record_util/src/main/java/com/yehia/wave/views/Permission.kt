package com.yehia.wave.views

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION_CODES.S_V2
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.example.awesomedialog.AwesomeDialog
import com.example.awesomedialog.body
import com.example.awesomedialog.onNegative
import com.example.awesomedialog.onPositive
import com.example.awesomedialog.title
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.yehia.wave_record_util.R

object Permission {
    const val PERMISSIONS_REQUEST_CODE = 2311

    fun Activity.showPermission2(
        startProcess: () -> Unit = { },
        permissionDenied: () -> Unit = { },
    ): Boolean {

        val isCameraPermission = isCameraPermission(this)
        val isStoragePermission = isStoragePermission(this)
        val isStoragePermission32 = isStoragePermission32(this)
        val isLimited = isReadMediaImagesLimited(this)

        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                val isStorage =
                    if (Build.VERSION.SDK_INT >= TIRAMISU) isStoragePermission32(this@showPermission2) && !isReadMediaImagesLimited(
                        this@showPermission2
                    )
                    else isStoragePermission(this@showPermission2)

                if (isCameraPermission(this@showPermission2) && isStorage) {
                    startProcess()
                }
            }

            override fun onPermissionDenied(deniedPermissions: List<String?>) {
                Log.d("TAG", "onPermissionDenied: ")
                permissionDenied()
            }
        }
        if (Build.VERSION.SDK_INT >= TIRAMISU) {
            TedPermission.create().setPermissionListener(permissionListener).setPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES,
            ).setDeniedTitle(getString(R.string.alert_pr))
                .setDeniedMessage(getString(R.string.reject_permission))
                .setGotoSettingButtonText(getString(R.string.setting)).check()
        } else {
            if (!isCameraPermission || !isStoragePermission) {
                TedPermission.create().setPermissionListener(permissionListener).setPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                ).setDeniedTitle(getString(R.string.alert_pr))
                    .setDeniedMessage(getString(R.string.reject_permission))
                    .setGotoSettingButtonText(getString(R.string.setting)).check()
            } else {
                startProcess()
            }
        }

        val isStorage =
            if (Build.VERSION.SDK_INT >= TIRAMISU) isStoragePermission32(this) && !isLimited
            else isStoragePermission(this)

        return isCameraPermission(this) && isStorage
    }

    private fun isReadMediaImagesLimited(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(MediaStore.Images.Media._ID)
            val cursor = context.contentResolver.query(uri, projection, null, null, null)
            val isLimitedAccess = cursor?.count == 0
            cursor?.close()
            isLimitedAccess
        } else {
            // For older versions, the concept of limited access doesn't apply
            false
        }
    }

    private fun isCameraPermission(context: Context): Boolean {
        return context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun isStoragePermission(context: Context): Boolean {
        return context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun isStoragePermission32(context: Context): Boolean {
        return Build.VERSION.SDK_INT >= TIRAMISU && (context.checkSelfPermission(
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun isMediaImageAccessAllowed(context: Context): Boolean {
        // Check permission
        val hasPermission =
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
//                context.checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
//            else
            context.checkSelfPermission(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED

        // Check storage state
        val state = Environment.getExternalStorageState()
        val isStorageReadable =
            Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state

        // Return true if permission granted and storage is readable
        return hasPermission && isStorageReadable
    }


    private fun Activity.confirmStringDialog(
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

    fun Activity.permissionStatus(): String {
        val explicitCameraPermissionRequired = !isCameraPermission(this)
        val readExternalStoragePermissionsRequired = !isStoragePermission(this)
        val readExternalStoragePermissionsRequired32 = !isStoragePermission32(this)
        val mediaImageAccessAllowed = isMediaImageAccessAllowed(this)

        return "Camera: $explicitCameraPermissionRequired , ExternalStoragePermissions:$readExternalStoragePermissionsRequired ,ExternalStoragePermissions32:$readExternalStoragePermissionsRequired32 ,MediaImageAccessAllowed:$mediaImageAccessAllowed"
    }
}