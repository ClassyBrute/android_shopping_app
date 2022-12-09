package com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user

import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.data.network.dto.ResponseTokenDto
import com.gd.intership.hdziedziura.androidshopingapp.data.network.dto.UserDto
import com.gd.intership.hdziedziura.androidshopingapp.domain.repository.AuthRepository
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import javax.inject.Inject

class LoginUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : LoginUseCase {

    override suspend fun execute(userDto: UserDto): Resource<ResponseTokenDto?> {
        return try {
            coroutineScope {
                when (val response = authRepository.Login(userDto)) {
                    is Resource.Success -> {
                        Resource.Success(ResponseTokenDto(response.data?.body()?.token!!))
                    }
                    else -> {
                        Resource.Error(response.message!!, ResponseTokenDto(""))
                    }
                }
            }
        } catch (e: IOException) {
            Resource.Error("connection error", ResponseTokenDto(""))
        }
    }
}
