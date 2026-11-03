package com.ksa.agence.di

import com.ksa.agence.repository.MainRepo
import org.koin.dsl.module

val repoModule = module {
    single {
        MainRepo(get())
    }
}