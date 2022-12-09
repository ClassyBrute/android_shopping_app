package com.gd.intership.hdziedziura.androidshopingapp.di

import com.gd.intership.hdziedziura.androidshopingapp.data.repositoryImpl.AuthRepositoryImpl
import com.gd.intership.hdziedziura.androidshopingapp.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module

@Module
internal interface AuthRepoModule {
    @Binds
    fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
}
