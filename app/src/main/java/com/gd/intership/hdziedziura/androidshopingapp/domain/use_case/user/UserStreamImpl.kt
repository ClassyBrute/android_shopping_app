package com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserStreamImpl @Inject constructor() : UserStream {

    private val userStatusFlow: MutableStateFlow<UserStatus> = MutableStateFlow(UserStatus.Authorized)
    override suspend fun emit(userStatus: UserStatus) {
        userStatusFlow.emit(userStatus)
    }

    override val userStatus: StateFlow<UserStatus>
        get() = userStatusFlow
}
