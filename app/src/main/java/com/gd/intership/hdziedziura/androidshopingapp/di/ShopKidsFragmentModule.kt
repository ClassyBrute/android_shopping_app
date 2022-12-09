package com.gd.intership.hdziedziura.androidshopingapp.di

import androidx.lifecycle.ViewModel
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.FragmentScope
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.ViewModelKey
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.ShopKidsFragment
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.ShopKidsViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface ShopKidsFragmentModule {
    @ContributesAndroidInjector(
        modules = [
            ShopKidsViewModelModule::class,
            CategoriesAllUseCaseModule::class,
            ProductsAllUseCaseModule::class,
            ProductRepoModule::class,
        ]
    )
    @FragmentScope
    fun contributeShopKidsFragment(): ShopKidsFragment
}

@Module
internal interface ShopKidsViewModelModule {
    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(ShopKidsViewModel::class)
    fun bindShopVM(viewModel: ShopKidsViewModel): ViewModel
}
