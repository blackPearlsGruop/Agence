package com.ksa.agence.common

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.ksa.agence.R

/**
 * Shared push notification setup for the merged app (client and member).
 *
 * One Firebase service receives every push; it opens the home of whichever side
 * the user picked (see [RoleManager]) and marks the intent with [EXTRA_OPEN_NOTIFICATIONS]
 * so that MainActivity can jump straight to the notifications page.
 */
object PushNotifications {

    const val CHANNEL_ID = "agence_general"

    const val EXTRA_OPEN_NOTIFICATIONS = "open_notifications"
    const val EXTRA_REDIRECT_ID = "redirect_id"
    const val EXTRA_CLICK_ACTION = "click_action"

    private const val PREFS_NAME = "Agence_Push"
    private const val KEY_TOKEN = "fcm_token"
    private const val KEY_ASKED = "permission_asked"
    private const val REQUEST_CODE_PERMISSION = 7301

    /** Creates the notification channel once (required on Android 8 and above). */
    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (manager.getNotificationChannel(CHANNEL_ID) != null) return
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = context.getString(R.string.notification_channel_desc)
            enableVibration(true)
            setShowBadge(true)
        }
        manager.createNotificationChannel(channel)
    }

    /**
     * Asks for the notification permission on Android 13 and above.
     * Asked once from the app; if the user refuses, Android keeps that choice
     * and the user can still turn it on from the system settings.
     */
    fun requestPermissionIfNeeded(activity: Activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        val granted = ContextCompat.checkSelfPermission(
            activity, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        if (granted) return

        val prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (prefs.getBoolean(KEY_ASKED, false)) return
        prefs.edit().putBoolean(KEY_ASKED, true).apply()

        ActivityCompat.requestPermissions(
            activity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE_PERMISSION
        )
    }

    /** Opens the notifications page when the activity was started from a notification tap. */
    fun handleOpenIntent(intent: Intent?, navController: NavController?) {
        if (intent?.getBooleanExtra(EXTRA_OPEN_NOTIFICATIONS, false) != true) return
        intent.removeExtra(EXTRA_OPEN_NOTIFICATIONS)
        try {
            if (navController?.currentDestination?.id != R.id.notificationFragment) {
                navController?.navigate(R.id.notificationFragment)
            }
        } catch (e: Exception) {
            // The current graph has no notifications page; stay on home.
        }
    }

    /** Latest FCM token, kept so it can be sent to the server with the next login. */
    fun saveToken(context: Context, token: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_TOKEN, token).apply()
    }

    fun savedToken(context: Context): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(KEY_TOKEN, null)
}
