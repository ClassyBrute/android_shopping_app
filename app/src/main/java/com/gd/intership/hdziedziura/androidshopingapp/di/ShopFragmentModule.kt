package com.gd.intership.hdziedziura.androidshopingapp.di

import androidx.lifecycle.ViewModel
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.FragmentScope
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.ViewModelKey
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.ShopFragment
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.ShopViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface ShopFragmentModule {
    @ContributesAndroidInjector(
        modules = [
            ShopViewModelModule::class,

        ]
    )
    @FragmentScope
    fun contributeShopFragment(): ShopFragment
}

@Module
internal interface ShopViewModelModule {
    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(ShopViewModel::class)
    fun bindShopVM(viewModel: ShopViewModel): ViewModel
}
