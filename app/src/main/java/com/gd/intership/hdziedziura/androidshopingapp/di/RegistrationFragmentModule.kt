package com.gd.intership.hdziedziura.androidshopingapp.di

import androidx.lifecycle.ViewModel
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.FragmentScope
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.ViewModelKey
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user.RegisterUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user.RegisterUseCaseImpl
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.RegisterFragment
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.RegisterViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface RegistrationFragmentModule {
    @ContributesAndroidInjector(
        modules = [
            AuthRepoModule::class,
            RegisterViewModelModule::class,
            RegisterUseCaseModule::class,
            UserValidationModule::class
        ]
    )
    @FragmentScope
    fun contributeRegisterFragment(): RegisterFragment
}

@Module
internal interface RegisterViewModelModule {
    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(RegisterViewModel::class)
    fun bindRegisterVM(viewModel: RegisterViewModel): ViewModel
}

@Module
internal interface RegisterUseCaseModule {
    @Binds
    fun bindRegisterUseCase(registerUseCase: RegisterUseCaseImpl): RegisterUseCase
}
