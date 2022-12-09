package com.gd.intership.hdziedziura.androidshopingapp.di

import android.content.res.Resources
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.FragmentScope
import com.gd.intership.hdziedziura.androidshopingapp.domain.validation.EmailValidationUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.validation.NameValidationUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.validation.PasswordValidationUseCase
import dagger.Module
import dagger.Provides

@Module
class UserValidationModule {

    @FragmentScope
    @Provides
    fun bindPasswordValidationUseCase(resources: Resources): PasswordValidationUseCase {
        return PasswordValidationUseCase(resources)
    }

    @FragmentScope
    @Provides
    fun bindNameValidationUseCase(resources: Resources): NameValidationUseCase {
        return NameValidationUseCase(resources)
    }

    @FragmentScope
    @Provides
    fun bindEmailValidationUseCase(resources: Resources): EmailValidationUseCase {
        return EmailValidationUseCase(resources)
    }
}
