package com.ksa.agenceCompany

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import com.ksa.agenceCompany.common.sharedprefrence.PreferencesUtils
import com.ksa.agenceCompany.di.*
import com.ksa.agenceCompany.receiver.FirebaseMessagingService.Companion.ANDROID_CHANNEL
import com.ksa.agenceCompany.receiver.FirebaseMessagingService.Companion.ANDROID_CHANNEL_ID
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AgenceCompanyApp : Application() {

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

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        startKoin {
            Log.i("StartKoin", "startkoin")
            androidContext(this@AgenceCompanyApp)
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