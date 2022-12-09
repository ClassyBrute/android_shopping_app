package com.gd.intership.hdziedziura.androidshopingapp.data.storage

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface DataStoreManager {
    suspend fun <T> writeToDataStore(key: Preferences.Key<T>, value: T)
    suspend fun <T> readFromDataStore(key: Preferences.Key<T>): Flow<T?>
    suspend fun clearUser()
}
