package com.gd.intership.hdziedziura.androidshopingapp.di

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.gd.intership.hdziedziura.androidshopingapp.data.database.AppDatabase
import com.gd.intership.hdziedziura.androidshopingapp.data.repositoryImpl.DatabaseRepositoryImpl
import com.gd.intership.hdziedziura.androidshopingapp.data.storage.DataStoreManager
import com.gd.intership.hdziedziura.androidshopingapp.data.storage.DataStoreManagerImpl
import com.gd.intership.hdziedziura.androidshopingapp.di.scope.ActivityScope
import com.gd.intership.hdziedziura.androidshopingapp.domain.repository.DatabaseRepository
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user.UserStream
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user.UserStreamImpl
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.MainActivity
import com.gd.intership.hdziedziura.androidshopingapp.presentation.vmFactory.AppViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module(includes = [ApiModule::class])
internal interface AppModule {
    @ContributesAndroidInjector(modules = [ActivityModule::class])
    @ActivityScope
    fun mainActivityInjector(): MainActivity

    @Binds
    fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory

    @Singleton
    @Binds
    fun bindUserStream(userStreamImpl: UserStreamImpl): UserStream

    companion object {
        @JvmStatic
        @Singleton
        @Provides
        fun provideDataStore(application: Application): DataStoreManager {
            return DataStoreManagerImpl(application.applicationContext)
        }

        @JvmStatic
        @Provides
        fun providesDatabaseUserManager(appDatabase: AppDatabase): DatabaseRepository {
            return DatabaseRepositoryImpl(appDatabase)
        }

        @JvmStatic
        @Provides
        fun provideRoomDb(application: Application): AppDatabase {
            return AppDatabase.getAppDatabaseInstance(application.applicationContext)
        }
    }
}
