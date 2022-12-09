package com.gd.intership.hdziedziura.androidshopingapp.data.repositoryImpl

import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.data.network.RetrofitApiService
import com.gd.intership.hdziedziura.androidshopingapp.data.network.dto.ResponseTokenDto
import com.gd.intership.hdziedziura.androidshopingapp.data.network.dto.UserDto
import com.gd.intership.hdziedziura.androidshopingapp.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: RetrofitApiService
) : AuthRepository {

    override suspend fun Login(userDto: UserDto): Resource<Response<ResponseTokenDto>> =
        withContext(Dispatchers.IO) {
            val result = api.loginUser(userDto)
            if (result.isSuccessful) {
                return@withContext Resource.Success(result)
            } else {
                return@withContext Resource.Error("error fetching", result)
            }
        }

    override suspend fun Register(userDto: UserDto): Resource<Response<UserDto>> =
        withContext(Dispatchers.IO) {
            val result = api.registerUser(userDto)
            if (result.isSuccessful) {
                return@withContext Resource.Success(result)
            } else {
                return@withContext Resource.Error("error fetching", result)
            }
        }
}
