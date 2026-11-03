package com.ksa.agence.member.di

import com.ksa.agence.member.repository.MainRepo
import org.koin.dsl.module

val repoModule = module {
    single {
        MainRepo(get())
    }
}