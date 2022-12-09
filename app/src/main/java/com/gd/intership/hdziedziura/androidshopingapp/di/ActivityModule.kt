package com.gd.intership.hdziedziura.androidshopingapp.di

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.ActivityScope
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.ViewModelKey
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.MainActivity
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.MainActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(
    includes = [
        RegistrationFragmentModule::class,
        LoginFragmentModule::class,
        ForgotPasswordFragmentModule::class,
        HomeFragmentModule::class,
        ShopFragmentModule::class,
        ShopProductListFragmentModule::class,
        ShopWomenFragmentModule::class,
        ShopMenFragmentModule::class,
        ShopKidsFragmentModule::class,
        SortBottomSheetFragmentModule::class,
        ShopFiltersFragmentModule::class,
        ProductDetailsFragmentModule::class,
        ActivityViewModelModule::class,
        BagFragmentModule::class,
        ProductReviewsFragmentModule::class,
        FavoriteFragmentModule::class
    ],
)
interface ActivityModule {

    @Binds
    fun bindsMainActivity(activity: MainActivity): AppCompatActivity

    companion object {
        @Provides
        fun resources(activity: AppCompatActivity): Resources = activity.resources
    }
}

@Module
internal interface ActivityViewModelModule {
    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(MainActivityViewModel::class)
    fun bindLoginVM(MainActivityViewModel: MainActivityViewModel): ViewModel
}
