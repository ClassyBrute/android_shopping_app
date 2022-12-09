package com.gd.intership.hdziedziura.androidshopingapp.di

import androidx.lifecycle.ViewModel
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.FragmentScope
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.ViewModelKey
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.ShopWomenFragment
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.ShopWomenViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface ShopWomenFragmentModule {
    @ContributesAndroidInjector(
        modules = [
            ShopWomenViewModelModule::class,
            CategoriesAllUseCaseModule::class,
            ProductsAllUseCaseModule::class,
            ProductRepoModule::class,
        ]
    )
    @FragmentScope
    fun contributeShopWomenFragment(): ShopWomenFragment
}

@Module
internal interface ShopWomenViewModelModule {
    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(ShopWomenViewModel::class)
    fun bindShopVM(viewModel: ShopWomenViewModel): ViewModel
}
