package com.ksa.agence.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.ksa.agence.R
import com.ksa.agence.ui.activity.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject

open class FirebaseMessagingService : FirebaseMessagingService() {

    private var redirectId = ""
    private var notificationId = 0
    private var clickAction = ""
    var title = ""
    private var nBody = ""
    lateinit var intent: Intent

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        try {
            // Check if message contains a data payload.
            if (remoteMessage.data.isNotEmpty()) {
                Log.i(TAG, "data payload: ${remoteMessage.data}")

                val params = remoteMessage.data
                val body = JSONObject(params as Map<*, *>)
                redirectId = remoteMessage.data["redirect_id"]?.toString() ?: ""
                clickAction = remoteMessage.data["click_action"]?.toString() ?: ""
                notificationId = body.optString("redirect_id", "0").toInt()
                title = body.optString("title", "")
                nBody = body.optString("body", "")

                intent = if (clickAction == "1" || clickAction == "2" || clickAction == "3") {
                    Intent(this, MainActivity::class.java).apply {
                        putExtra("redirect_id", redirectId)
                        putExtra("click_action", clickAction)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    }
                } else {
                    Intent(this, MainActivity::class.java)
                }
            }

            // notification payload
            remoteMessage.notification?.let {
                val mRedirectId = redirectId
                title = it.title ?: ""
                nBody = it.body ?: ""

                intent = if (clickAction == "1" || clickAction == "2" || clickAction == "3") {
                    Log.i(TAG, "clickActionNotificationOrderStep : $mRedirectId")
                    Intent(this, MainActivity::class.java).apply {
                        putExtra("redirect_id", redirectId)
                        putExtra("click_action", clickAction)
                        putExtra("title", title)
                        putExtra("body", nBody)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    }
                } else {
                    Intent(this, MainActivity::class.java).apply {
                        putExtra("redirect_id", mRedirectId)
                        putExtra("click_action", clickAction)
                        putExtra("title", title)
                        putExtra("body", nBody)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    }
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                showNotificationsCancel(title, nBody, intent)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error handling message: ${e.localizedMessage}", e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotificationsCancel(title: String, content: String, intent: Intent) {
        val myNotifications = NotificationCompat.Builder(
            this, ANDROID_CHANNEL_ID
        ).setContentTitle(title).setContentText(content).setTicker("Notification!")
            .setWhen(System.currentTimeMillis()).setAutoCancel(true)
            .setPriority(Notification.PRIORITY_MAX).setSmallIcon(R.drawable.logo_splash)

        // Load the logo image as Bitmap
        val largeIconBitmap = BitmapFactory.decodeResource(resources, R.drawable.logo_splash)

        // Set the large icon for the notification
        myNotifications.setLargeIcon(largeIconBitmap)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE
        )

        myNotifications.setContentIntent(pendingIntent).setDefaults(Notification.DEFAULT_SOUND)
        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(
            ANDROID_CHANNEL_ID, ANDROID_CHANNEL, importance
        )
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.DKGRAY
        notificationChannel.setShowBadge(true)
        notificationChannel.enableVibration(true)
        notificationChannel.vibrationPattern =
            longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        myNotifications.setChannelId(ANDROID_CHANNEL_ID)
        notificationManager.createNotificationChannel(notificationChannel)
        notificationManager.notify(0 /* Request Code */, myNotifications.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Handle the new FCM token here
        Log.d(TAG, "Refreshed token: $token")
    }

    companion object {
        private const val TAG = "FirebaseMessagingService"
        const val ANDROID_CHANNEL_ID = "1000123"
        const val ANDROID_CHANNEL = "my_time_app_channel"
    }
}
