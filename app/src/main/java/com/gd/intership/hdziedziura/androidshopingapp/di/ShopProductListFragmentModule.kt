package com.gd.intership.hdziedziura.androidshopingapp.di

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.FragmentScope
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.ViewModelKey
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.ShopProductListFragment
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.ShopProductListViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface ShopProductListFragmentModule {
    @ContributesAndroidInjector(
        modules = [
            ShopProductListViewModelModule::class,
            ProductRepoModule::class,
            ProductCategoryUseCaseModule::class,
            ProductSubcategoryUseCaseModule::class,
            BundleModule::class,
            ProductFavoriteDatabaseInsertUseCaseModule::class,
            ProductFavoriteDatabaseAllUseCaseModule::class,
            ProductFavoriteDatabaseDeleteUseCaseModule::class
        ]
    )
    @FragmentScope
    fun contributeShopProductListFragment(): ShopProductListFragment
}

@Module
internal interface BundleModule {
    companion object {
        @Provides
        fun provideBundle(shopProductListFragment: ShopProductListFragment): Bundle? {
            return shopProductListFragment.arguments
        }
    }
}

@Module
internal interface ShopProductListViewModelModule {
    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(ShopProductListViewModel::class)
    fun bindShopVM(viewModel: ShopProductListViewModel): ViewModel
}
