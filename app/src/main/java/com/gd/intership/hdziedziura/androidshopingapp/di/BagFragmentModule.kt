package com.gd.intership.hdziedziura.androidshopingapp.di

import androidx.lifecycle.ViewModel
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.FragmentScope
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.ViewModelKey
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsDatabaseDecreaseUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsDatabaseDecreaseUseCaseImpl
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsDatabaseDeleteUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsDatabaseDeleteUseCaseImpl
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsDatabaseIncreaseUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsDatabaseIncreaseUseCaseImpl
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.BagFragment
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.bag.BagViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface BagFragmentModule {
    @ContributesAndroidInjector(
        modules = [
            BagViewModelModule::class,
            ProductDatabaseAllUseCaseModule::class,
            ProductDatabaseDeleteUseCaseModule::class,
            ProductDatabaseIncreaseUseCaseModule::class,
            ProductDatabaseDecreaseUseCaseModule::class,
            ProductFavoriteDatabaseInsertUseCaseModule::class,
            ProductFavoriteDatabaseAllUseCaseModule::class,
            ProductFavoriteDatabaseDeleteUseCaseModule::class
        ]
    )
    @FragmentScope
    fun contributeBagFragment(): BagFragment
}

@Module
internal interface BagViewModelModule {
    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(BagViewModel::class)
    fun bindBagVM(viewModel: BagViewModel): ViewModel
}

@Module
internal interface ProductDatabaseDeleteUseCaseModule {
    @Binds
    fun bindProductDatabaseDeleteUseCase(productsDatabaseDeleteUseCase: ProductsDatabaseDeleteUseCaseImpl): ProductsDatabaseDeleteUseCase
}

@Module
internal interface ProductDatabaseIncreaseUseCaseModule {
    @Binds
    fun bindProductDatabaseIncreaseUseCase(productsDatabaseIncreaseUseCase: ProductsDatabaseIncreaseUseCaseImpl): ProductsDatabaseIncreaseUseCase
}

@Module
internal interface ProductDatabaseDecreaseUseCaseModule {
    @Binds
    fun bindProductDatabaseDecreaseUseCase(productsDatabaseDecreaseUseCase: ProductsDatabaseDecreaseUseCaseImpl): ProductsDatabaseDecreaseUseCase
}
