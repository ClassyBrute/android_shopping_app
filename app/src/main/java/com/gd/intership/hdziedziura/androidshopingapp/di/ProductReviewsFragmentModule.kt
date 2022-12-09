package com.gd.intership.hdziedziura.androidshopingapp.di

import androidx.lifecycle.ViewModel
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.FragmentScope
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.ViewModelKey
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.ProductReviewsFragment
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.reviews.ProductReviewsViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface ProductReviewsFragmentModule {
    @ContributesAndroidInjector(
        modules = [
            ProductReviewsViewModelModule::class,
            ProductRepoModule::class,
            ProductCategoryUseCaseModule::class,
        ]
    )
    @FragmentScope
    fun contributeProductReviewsFragment(): ProductReviewsFragment
}

@Module
internal interface ProductReviewsViewModelModule {
    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(ProductReviewsViewModel::class)
    fun bindReviewsVM(viewModel: ProductReviewsViewModel): ViewModel
}
