package com.gd.intership.hdziedziura.androidshopingapp.di

import androidx.lifecycle.ViewModel
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.FragmentScope
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.ViewModelKey
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.SortBottomSheetFragment
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.SortBottomSheetViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface SortBottomSheetFragmentModule {
    @ContributesAndroidInjector(
        modules = [
            SortBottomSheetViewModelModule::class
        ]
    )
    @FragmentScope
    fun contributeSortBottomSheetFragment(): SortBottomSheetFragment
}

@Module
internal interface SortBottomSheetViewModelModule {
    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(SortBottomSheetViewModel::class)
    fun bindShopVM(viewModel: SortBottomSheetViewModel): ViewModel
}
