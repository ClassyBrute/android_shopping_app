package com.gd.intership.hdziedziura.androidshopingapp.di

import com.gd.intership.hdziedziura.androidshopingapp.di.scope.FragmentScope
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.ShopFiltersFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ShopFiltersFragmentModule {
    @ContributesAndroidInjector(
        modules = [
            ShopProductListViewModelModule::class,
            ProductRepoModule::class,
            ProductCategoryUseCaseModule::class,
            ProductSubcategoryUseCaseModule::class,
        ]
    )
    @FragmentScope
    fun contributeShopFiltersFragment(): ShopFiltersFragment
}
