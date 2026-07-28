package com.ksa.agence.di

import com.ksa.agence.viewModels.AuthenticationViewModel
import com.ksa.agence.viewModels.HomeViewModel
import com.ksa.agence.viewModels.InfoViewModel
import com.ksa.agence.viewModels.NotificationViewModel
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