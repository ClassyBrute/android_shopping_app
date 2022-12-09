package com.gd.intership.hdziedziura.androidshopingapp.di

import androidx.lifecycle.ViewModel
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.FragmentScope
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.ViewModelKey
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.ShopMenFragment
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.ShopMenViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface ShopMenFragmentModule {
    @ContributesAndroidInjector(
        modules = [
            ShopMenViewModelModule::class,
            CategoriesAllUseCaseModule::class,
            ProductsAllUseCaseModule::class,
            ProductRepoModule::class,
        ]
    )
    @FragmentScope
    fun contributeShopMenFragment(): ShopMenFragment
}

@Module
internal interface ShopMenViewModelModule {
    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(ShopMenViewModel::class)
    fun bindShopVM(viewModel: ShopMenViewModel): ViewModel
}
