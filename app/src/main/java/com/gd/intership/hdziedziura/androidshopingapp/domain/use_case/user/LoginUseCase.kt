package com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user

import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.data.network.dto.ResponseTokenDto
import com.gd.intership.hdziedziura.androidshopingapp.data.network.dto.UserDto

interface LoginUseCase {

    suspend fun execute(userDto: UserDto): Resource<ResponseTokenDto?>
}
