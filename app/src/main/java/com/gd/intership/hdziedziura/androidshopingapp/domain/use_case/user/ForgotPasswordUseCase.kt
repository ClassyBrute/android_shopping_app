package com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user

import com.gd.intership.hdziedziura.androidshopingapp.data.network.dto.UserDto

interface ForgotPasswordUseCase {

    fun execute(userDto: UserDto)
}
