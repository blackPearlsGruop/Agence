package com.ksa.agenceCompany.di

import com.ksa.agenceCompany.common.sharedprefrence.PreferencesUtils
import org.koin.dsl.module

val sharedPreferencesModule = module {
    single {
        PreferencesUtils(get())
    }
}