package com.gd.intership.hdziedziura.androidshopingapp.domain.repository

import com.gd.intership.hdziedziura.androidshopingapp.data.network.dto.ProductDto
import retrofit2.Response

interface ProductRepository {
    suspend fun allProducts(): Response<List<ProductDto>>

    suspend fun allCategories(): Response<List<String>>

    suspend fun categoryProducts(category: String): Response<List<ProductDto>>
}
