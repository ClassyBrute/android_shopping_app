package com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user

import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.data.network.dto.UserDto
import com.gd.intership.hdziedziura.androidshopingapp.domain.repository.AuthRepository
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import javax.inject.Inject

class RegisterUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : RegisterUseCase {

    override suspend fun execute(userDto: UserDto): Resource<String?> {
        return try {
            coroutineScope {
                when (val result = authRepository.Register(userDto)) {
                    is Resource.Success -> {
//                        Resource.Success(result.data.body())
                        Resource.Success("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjoibW9yXzIzMTQiLCJpYXQiOjE2NjIxMTYxNzl9.rsx4hdRs_OfOLRqTxwRIZfakRG2QOaGxo5jr-P7Ug1Y")
                    }
                    is Resource.Error -> { Resource.Error(result.message!!, "") }
                    else -> { Resource.Error("", "") }
                }
            }
        } catch (e: IOException) {
            Resource.Error("connection error", "")
        }
    }
}
