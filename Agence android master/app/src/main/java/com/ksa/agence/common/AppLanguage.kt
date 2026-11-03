package com.ksa.agence.common

import android.content.Context

/**
 * The client and member sides each keep their own preferences file.
 * The app language must be the same on both sides, so it is always
 * saved to both files from here.
 */
object AppLanguage {

    /** Saved language, or the device language when nothing is saved yet (same rule as BaseActivity). */
    fun current(context: Context): String {
        val deviceLang = if (java.util.Locale.getDefault().language == "ar") "ar" else "en"
        return com.ksa.agence.common.sharedprefrence.PreferencesUtils(context).getString(LANG, deviceLang)
            ?: deviceLang
    }

    fun set(context: Context, lang: String) {
        com.ksa.agence.common.sharedprefrence.PreferencesUtils(context).putString(LANG, lang)
        com.ksa.agence.member.common.sharedprefrence.PreferencesUtils(context).putString(LANG, lang)
    }

    /**
     * A context that follows the app language even outside activities
     * (for example in network interceptors), so messages match the chosen language.
     */
    fun localized(context: Context): Context {
        val locale = java.util.Locale(current(context))
        val config = android.content.res.Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }

    /** Switches between Arabic and English and returns the new language. */
    fun toggle(context: Context): String {
        val newLang = if (current(context) == "ar") "en" else "ar"
        set(context, newLang)
        return newLang
    }
}
