package com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user

import com.gd.intership.hdziedziura.androidshopingapp.data.network.dto.UserDto
import com.gd.intership.hdziedziura.androidshopingapp.domain.repository.AuthRepository
import javax.inject.Inject

class ForgotPasswordUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : ForgotPasswordUseCase {

    override fun execute(userDto: UserDto) {
        TODO("Not yet implemented")
    }
}
