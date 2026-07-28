package com.ksa.agenceCompany.di

import com.ksa.agenceCompany.viewModels.AuthenticationViewModel
import com.ksa.agenceCompany.viewModels.HomeViewModel
import com.ksa.agenceCompany.viewModels.InfoViewModel
import com.ksa.agenceCompany.viewModels.NotificationViewModel
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