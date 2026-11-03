package com.ksa.agence.receiver

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.RemoteMessage
import com.ksa.agence.R
import com.ksa.agence.common.PushNotifications
import com.ksa.agence.common.RoleManager

/**
 * The only Firebase messaging service of the merged app.
 * It serves both sides: the tap opens the client or member home depending on the
 * role the user picked, and that home then opens its notifications page.
 */
open class FirebaseMessagingService : com.google.firebase.messaging.FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        try {
            val data = remoteMessage.data
            Log.i(TAG, "push received, data: $data")

            val title = remoteMessage.notification?.title
                ?: data["title"]
                ?: getString(R.string.notification_default_title)
            val body = remoteMessage.notification?.body ?: data["body"].orEmpty()
            if (body.isBlank() && remoteMessage.notification == null && data["title"] == null) return

            val redirectId = data["redirect_id"] ?: data["type_id"].orEmpty()
            val clickAction = data["click_action"] ?: data["type"].orEmpty()

            showNotification(title, body, redirectId, clickAction)
        } catch (e: Exception) {
            Log.e(TAG, "Error handling message: ${e.localizedMessage}", e)
        }
    }

    private fun showNotification(title: String, body: String, redirectId: String, clickAction: String) {
        PushNotifications.createChannel(this)

        val target = if (RoleManager.getRole(this) == RoleManager.MEMBER) {
            com.ksa.agence.member.ui.activity.MainActivity::class.java
        } else {
            com.ksa.agence.ui.activity.MainActivity::class.java
        }

        val intent = Intent(this, target).apply {
            putExtra(PushNotifications.EXTRA_OPEN_NOTIFICATIONS, true)
            putExtra(PushNotifications.EXTRA_REDIRECT_ID, redirectId)
            putExtra(PushNotifications.EXTRA_CLICK_ACTION, clickAction)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        // Each notification gets its own id, so a new one does not replace the previous one
        val notificationId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()

        val pendingIntent = PendingIntent.getActivity(
            this, notificationId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, PushNotifications.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_notification)
            .setColor(ContextCompat.getColor(this, R.color.primary))
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(this).notify(notificationId, notification)
        } catch (e: SecurityException) {
            // Notification permission was refused by the user
            Log.w(TAG, "Notification permission not granted", e)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Kept locally; the server receives it with the next login or OTP verification
        PushNotifications.saveToken(this, token)
        Log.d(TAG, "Refreshed token received")
    }

    companion object {
        private const val TAG = "AgenceMessaging"
    }
}
