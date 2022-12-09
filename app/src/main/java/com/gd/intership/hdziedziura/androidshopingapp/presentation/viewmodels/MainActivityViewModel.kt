package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.gd.intership.hdziedziura.androidshopingapp.data.storage.DataStoreManager
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user.UserStatus
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user.UserStream
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val userStream: UserStream
) : ViewModel(), DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        checkUserAuth()
    }

    private fun checkUserAuth() {
        runBlocking {
            dataStoreManager.clearUser()
            userStream.emit(UserStatus.NonAuthorized)
        }
    }
}
