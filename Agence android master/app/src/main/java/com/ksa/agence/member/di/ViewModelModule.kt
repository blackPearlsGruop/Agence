package com.ksa.agence.member.di

import com.ksa.agence.member.viewModels.AuthenticationViewModel
import com.ksa.agence.member.viewModels.HomeViewModel
import com.ksa.agence.member.viewModels.InfoViewModel
import com.ksa.agence.member.viewModels.NotificationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authenticationViewModelModule = module {
    viewModel {
        AuthenticationViewModel(get(),get())
    }
}

val homeViewModelModule = module {
    viewModel {
        HomeViewModel(get(),get())
    }
}
val infoViewModelModule = module {
    viewModel {
        InfoViewModel(get(),get())
    }
}

val notificationVViewModelModule = module {
    viewModel {
        NotificationViewModel(get(),get())
    }
}