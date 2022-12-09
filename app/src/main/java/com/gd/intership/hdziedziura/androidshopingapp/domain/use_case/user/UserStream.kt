package com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user

import kotlinx.coroutines.flow.StateFlow

interface UserStream {
    val userStatus: StateFlow<UserStatus>
    suspend fun emit(userStatus: UserStatus)
}

sealed class UserStatus {
    object Authorized : UserStatus()
    object NonAuthorized : UserStatus()
}
