package com.ksa.agenceCompany.di

import com.ksa.agenceCompany.repository.MainRepo
import org.koin.dsl.module

val repoModule = module {
    single {
        MainRepo(get())
    }
}