package com.ksa.agence.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import com.ksa.agence.common.sharedprefrence.PreferencesUtils
import com.ksa.agence.di.*
import com.ksa.agence.receiver.FirebaseMessagingService.Companion.ANDROID_CHANNEL
import com.ksa.agence.receiver.FirebaseMessagingService.Companion.ANDROID_CHANNEL_ID
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter

class AgenceApp : Application() {

    private lateinit var notificationManager: NotificationManager

    val sharedPreference: PreferencesUtils by lazy {
        PreferencesUtils(this)
    }

    companion object {
        var context: Context? = null
        lateinit var pref: PreferencesUtils
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        pref = PreferencesUtils(this)

        // ── TEMPORARY DEBUG: on any uncaught crash (any thread), show the
        // full stack trace in a plain on-device screen (no Logcat / Device
        // Explorer needed), then still write it to a file and hand off to
        // whatever handler was already installed. Remove this block once
        // Remove this block once the crash is found and fixed.
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                val sw = StringWriter()
                throwable.printStackTrace(PrintWriter(sw))
                val fullTrace = "Thread: ${thread.name}\nTime: ${System.currentTimeMillis()}\n\n${sw}"

                val file = File(getExternalFilesDir(null), "crash_debug.txt")
                file.writeText(fullTrace)
                Log.e("MYCRASH", fullTrace)

                val intent = android.content.Intent(
                    this,
                    com.ksa.agence.ui.activity.CrashDisplayActivity::class.java
                ).apply {
                    putExtra(com.ksa.agence.ui.activity.CrashDisplayActivity.EXTRA_TRACE, fullTrace)
                    addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
                startActivity(intent)
            } catch (inner: Throwable) {
                Log.e("MYCRASH", "Failed to show crash screen", inner)
            }
            Runtime.getRuntime().exit(1)
        }

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        startKoin {
            Log.i("StartKoin", "startkoin")
            androidContext(this@AgenceApp)
            modules(
                listOf(
                    repoModule,
                    sharedPreferencesModule,
                    appModule,
                    authenticationViewModelModule,
                    homeViewModelModule,
                    infoViewModelModule,
                    notificationVViewModelModule
                )
            )
        }
        createAppChannel()
    }

    private fun createAppChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(
                ANDROID_CHANNEL_ID ,ANDROID_CHANNEL, NotificationManager.IMPORTANCE_HIGH
            )

            val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            notificationChannel.setSound(
                defaultSound,
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_MEDIA).build()
            )

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}