package com.ksa.agence.member.di

import com.ksa.agence.member.common.sharedprefrence.PreferencesUtils
import org.koin.dsl.module

val sharedPreferencesModule = module {
    single {
        PreferencesUtils(get())
    }
}