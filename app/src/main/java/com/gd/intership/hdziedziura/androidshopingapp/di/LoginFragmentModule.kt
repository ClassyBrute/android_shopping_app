package com.gd.intership.hdziedziura.androidshopingapp.di

import androidx.lifecycle.ViewModel
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.FragmentScope
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.ViewModelKey
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user.LoginUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user.LoginUseCaseImpl
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.LoginFragment
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface LoginFragmentModule {
    @ContributesAndroidInjector(
        modules = [
            AuthRepoModule::class,
            LoginViewModelModule::class,
            LoginUseCaseModule::class,
            UserValidationModule::class
        ]
    )
    @FragmentScope
    fun contributeLoginFragment(): LoginFragment
}

@Module
internal interface LoginViewModelModule {
    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(LoginViewModel::class)
    fun bindLoginVM(viewModel: LoginViewModel): ViewModel
}

@Module
internal interface LoginUseCaseModule {
    @Binds
    fun bindLoginUseCase(loginUseCase: LoginUseCaseImpl): LoginUseCase
}
