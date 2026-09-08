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

        // ── TEMPORARY DEBUG: write every uncaught crash (any thread) to a
        // file on device storage, then still hand off to whatever handler
        // was already installed (e.g. Crashlytics) so normal behavior
        // continues. Remove this block once the crash is found and fixed.
        val previousHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                val sw = StringWriter()
                throwable.printStackTrace(PrintWriter(sw))
                val file = File(getExternalFilesDir(null), "crash_debug.txt")
                file.writeText(
                    "Thread: ${thread.name}\n" +
                            "Time: ${System.currentTimeMillis()}\n\n" +
                            sw.toString()
                )
                Log.e("MYCRASH", sw.toString())
            } catch (inner: Throwable) {
                Log.e("MYCRASH", "Failed to write crash file", inner)
            }
            previousHandler?.uncaughtException(thread, throwable)
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