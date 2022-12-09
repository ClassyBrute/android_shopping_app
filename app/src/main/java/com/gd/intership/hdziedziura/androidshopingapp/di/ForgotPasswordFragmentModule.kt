package com.gd.intership.hdziedziura.androidshopingapp.di

import androidx.lifecycle.ViewModel
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.FragmentScope
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.ViewModelKey
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user.ForgotPasswordUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user.ForgotPasswordUseCaseImpl
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.ForgotPasswordFragment
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.ForgotPasswordViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface ForgotPasswordFragmentModule {
    @ContributesAndroidInjector(
        modules = [
            AuthRepoModule::class,
            ForgotPasswordViewModelModule::class,
            ForgotPasswordUseCaseModule::class,
            UserValidationModule::class
        ]
    )
    @FragmentScope
    fun contributeForgotPasswordFragment(): ForgotPasswordFragment
}

@Module
internal interface ForgotPasswordViewModelModule {
    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(ForgotPasswordViewModel::class)
    fun bindForgotPasswordVM(viewModel: ForgotPasswordViewModel): ViewModel
}

@Module
internal interface ForgotPasswordUseCaseModule {
    @Binds
    fun bindForgotPasswordUseCase(forgotPasswordUseCase: ForgotPasswordUseCaseImpl): ForgotPasswordUseCase
}
