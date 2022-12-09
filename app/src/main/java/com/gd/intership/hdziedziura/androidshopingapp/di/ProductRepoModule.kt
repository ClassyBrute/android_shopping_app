package com.gd.intership.hdziedziura.androidshopingapp.di

import com.gd.intership.hdziedziura.androidshopingapp.data.repositoryImpl.ProductRepositoryImpl
import com.gd.intership.hdziedziura.androidshopingapp.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module

@Module
internal interface ProductRepoModule {
    @Binds
    fun bindProductRepository(productRepositoryImpl: ProductRepositoryImpl): ProductRepository
}
