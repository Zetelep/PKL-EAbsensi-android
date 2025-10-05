package com.zulfa.eabsensi.presentation.di

import com.zulfa.eabsensi.core.domain.usecase.AuthInteractor
import com.zulfa.eabsensi.core.domain.usecase.AuthUseCase
import com.zulfa.eabsensi.presentation.auth.login.LoginViewModel
import com.zulfa.eabsensi.presentation.main.history.HistoryViewModel
import com.zulfa.eabsensi.presentation.main.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module{
    factory <AuthUseCase>{ AuthInteractor(get()) }
}

val viewModelModule = module{
    viewModel { LoginViewModel(get()) }
    viewModel { HistoryViewModel(get(),get()) }
    viewModel { HomeViewModel(get()) }
}

