package com.gd.intership.hdziedziura.androidshopingapp.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import java.io.IOException
import java.util.concurrent.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManagerImpl @Inject constructor(
    private val appContext: Context
) : DataStoreManager {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("dataStore")

    override suspend fun <T> writeToDataStore(key: Preferences.Key<T>, value: T) {
        try {
            appContext.dataStore.edit {
                it[key] = value
            }
            println("write successful")
        } catch (e: CancellationException) {
            println("cancelled")
        } catch (e: IOException) {
            println("failed to write")
        } catch (e: Exception) {
            println("failed to write")
        }
    }

    override suspend fun <T> readFromDataStore(key: Preferences.Key<T>): Flow<T> {
        return appContext.dataStore.data.mapNotNull {
            it[key]
        }
    }

    override suspend fun clearUser() {
        appContext.dataStore.edit {
            it[(stringPreferencesKey(DataKeys.username))] = ""
            it[(stringPreferencesKey(DataKeys.email))] = ""
            it[(stringPreferencesKey(DataKeys.password))] = ""
            it[(stringPreferencesKey(DataKeys.token))] = ""
        }
    }
}
