package com.gd.intership.hdziedziura.androidshopingapp.di

import androidx.lifecycle.ViewModel
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.FragmentScope
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.ViewModelKey
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.CategoriesAllUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.CategoriesAllUseCaseImpl
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductCategoryUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductCategoryUseCaseImpl
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductSubcategoryUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductSubcategoryUseCaseImpl
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsAllUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsAllUseCaseImpl
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsDatabaseAllUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsDatabaseAllUseCaseImpl
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsDatabaseInsertUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsDatabaseInsertUseCaseImpl
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsFavoriteDatabaseAllUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsFavoriteDatabaseAllUseCaseImpl
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsFavoriteDatabaseDeleteUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsFavoriteDatabaseDeleteUseCaseImpl
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsFavoriteDatabaseInsertUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsFavoriteDatabaseInsertUseCaseImpl
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.HomeFragment
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface HomeFragmentModule {
    @ContributesAndroidInjector(
        modules = [
            ProductRepoModule::class,
            HomeViewModelModule::class,
            ProductsAllUseCaseModule::class,
            CategoriesAllUseCaseModule::class,
            ProductCategoryUseCaseModule::class,
            ProductSubcategoryUseCaseModule::class,
            ProductFavoriteDatabaseInsertUseCaseModule::class,
            ProductFavoriteDatabaseAllUseCaseModule::class,
            ProductFavoriteDatabaseDeleteUseCaseModule::class
        ]
    )
    @FragmentScope
    fun contributeHomeFragment(): HomeFragment
}

@Module
internal interface HomeViewModelModule {
    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(HomeViewModel::class)
    fun bindHomeVM(viewModel: HomeViewModel): ViewModel
}

@Module
internal interface ProductsAllUseCaseModule {
    @Binds
    fun bindProductsAllUseCase(productsAllUseCase: ProductsAllUseCaseImpl): ProductsAllUseCase
}

@Module
internal interface CategoriesAllUseCaseModule {
    @Binds
    fun bindCategoriesAllUseCase(categoriesAllUseCase: CategoriesAllUseCaseImpl): CategoriesAllUseCase
}

@Module
internal interface ProductCategoryUseCaseModule {
    @Binds
    fun bindProductCategoryUseCase(productCategoryUseCase: ProductCategoryUseCaseImpl): ProductCategoryUseCase
}

@Module
internal interface ProductSubcategoryUseCaseModule {
    @Binds
    fun bindProductSubcategoryUseCase(productSubcategoryUseCase: ProductSubcategoryUseCaseImpl): ProductSubcategoryUseCase
}

@Module
internal interface ProductDatabaseInsertUseCaseModule {
    @Binds
    fun bindProductDatabaseInsertUseCase(productsDatabaseInsertUseCase: ProductsDatabaseInsertUseCaseImpl): ProductsDatabaseInsertUseCase
}

@Module
internal interface ProductDatabaseAllUseCaseModule {
    @Binds
    fun bindProductDatabaseAllUseCase(productsDatabaseAllUseCase: ProductsDatabaseAllUseCaseImpl): ProductsDatabaseAllUseCase
}

@Module
internal interface ProductFavoriteDatabaseAllUseCaseModule {
    @Binds
    fun bindProductFavoritesDatabaseAllUseCase(productsFavoritesDatabaseAllUseCase: ProductsFavoriteDatabaseAllUseCaseImpl): ProductsFavoriteDatabaseAllUseCase
}

@Module
internal interface ProductFavoriteDatabaseInsertUseCaseModule {
    @Binds
    fun bindProductFavoritesDatabaseInsertUseCase(productsFavoritesDatabaseInsertUseCase: ProductsFavoriteDatabaseInsertUseCaseImpl): ProductsFavoriteDatabaseInsertUseCase
}

@Module
internal interface ProductFavoriteDatabaseDeleteUseCaseModule {
    @Binds
    fun bindProductFavoriteDatabaseDeleteUseCase(productsFavoriteDatabaseDeleteUseCase: ProductsFavoriteDatabaseDeleteUseCaseImpl): ProductsFavoriteDatabaseDeleteUseCase
}
