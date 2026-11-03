package com.ksa.agence.member.app

import android.app.Application
import android.content.Context
import com.ksa.agence.member.common.sharedprefrence.PreferencesUtils

/**
 * Holder for the member (company) side after merging into the single Agence app.
 * The real Application class is com.ksa.agence.app.AgenceApp, which calls init()
 * on startup and loads the member Koin modules.
 */
class AgenceCompanyApp private constructor() {

    companion object {
        var context: Context? = null
        lateinit var pref: PreferencesUtils

        val sharedPreference: PreferencesUtils
            get() = pref

        fun init(app: Application) {
            context = app
            pref = PreferencesUtils(app)
        }
    }
}
