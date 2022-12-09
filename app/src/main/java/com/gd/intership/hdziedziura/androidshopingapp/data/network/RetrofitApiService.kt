package com.gd.intership.hdziedziura.androidshopingapp.data.network

import com.gd.intership.hdziedziura.androidshopingapp.data.network.dto.ProductDto
import com.gd.intership.hdziedziura.androidshopingapp.data.network.dto.ResponseTokenDto
import com.gd.intership.hdziedziura.androidshopingapp.data.network.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitApiService {

    @POST("users")
    suspend fun registerUser(@Body userDto: UserDto): Response<UserDto>

    @POST("auth/login")
    suspend fun loginUser(@Body userDto: UserDto): Response<ResponseTokenDto>

    @GET("products")
    suspend fun allProducts(): Response<List<ProductDto>>

    @GET("products/categories")
    suspend fun allCategories(): Response<List<String>>

    @GET("products/category/{category}")
    suspend fun categoryProducts(@Path("category") category: String): Response<List<ProductDto>>
}
