package com.gd.intership.hdziedziura.androidshopingapp.domain.repository

import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.data.network.dto.ResponseTokenDto
import com.gd.intership.hdziedziura.androidshopingapp.data.network.dto.UserDto
import retrofit2.Response

interface AuthRepository {

    suspend fun Login(userDto: UserDto): Resource<Response<ResponseTokenDto>>

    suspend fun Register(userDto: UserDto): Resource<Response<UserDto>>
}
