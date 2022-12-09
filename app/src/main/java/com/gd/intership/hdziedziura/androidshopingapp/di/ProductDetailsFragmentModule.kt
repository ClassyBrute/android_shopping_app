package com.gd.intership.hdziedziura.androidshopingapp.di

import androidx.lifecycle.ViewModel
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.FragmentScope
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.ViewModelKey
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.ProductDetailsFragment
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.details.ProductDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface ProductDetailsFragmentModule {
    @ContributesAndroidInjector(
        modules = [
            ProductDetailsViewModelModule::class,
            ProductRepoModule::class,
            ProductCategoryUseCaseModule::class,
            ProductDatabaseInsertUseCaseModule::class,
            ProductFavoriteDatabaseInsertUseCaseModule::class,
            ProductFavoriteDatabaseAllUseCaseModule::class,
            ProductFavoriteDatabaseDeleteUseCaseModule::class
        ]
    )
    @FragmentScope
    fun contributeProductDetailsFragment(): ProductDetailsFragment
}

@Module
internal interface ProductDetailsViewModelModule {
    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(ProductDetailsViewModel::class)
    fun bindShopVM(viewModel: ProductDetailsViewModel): ViewModel
}
