package com.ksa.agence.di

import com.ksa.agence.common.sharedprefrence.PreferencesUtils
import org.koin.dsl.module

val sharedPreferencesModule = module {
    single {
        PreferencesUtils(get())
    }
}