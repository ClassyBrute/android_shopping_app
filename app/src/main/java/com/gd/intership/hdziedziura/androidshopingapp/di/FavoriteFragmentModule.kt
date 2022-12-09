package com.gd.intership.hdziedziura.androidshopingapp.di

import androidx.lifecycle.ViewModel
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.FragmentScope
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.ViewModelKey
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.FavoritesFragment
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.favorites.FavoritesViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface FavoriteFragmentModule {
    @ContributesAndroidInjector(
        modules = [
            FavoritesViewModelModule::class,
            ProductFavoriteDatabaseAllUseCaseModule::class,
            ProductFavoriteDatabaseDeleteUseCaseModule::class,
            ProductDatabaseInsertUseCaseModule::class
        ]
    )
    @FragmentScope
    fun contributeFavoritesFragment(): FavoritesFragment
}

@Module
internal interface FavoritesViewModelModule {
    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(FavoritesViewModel::class)
    fun bindFavoritesVM(viewModel: FavoritesViewModel): ViewModel
}
