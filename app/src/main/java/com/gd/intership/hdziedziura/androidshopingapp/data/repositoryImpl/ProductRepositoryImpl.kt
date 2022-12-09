package com.gd.intership.hdziedziura.androidshopingapp.data.repositoryImpl

import com.gd.intership.hdziedziura.androidshopingapp.data.network.RetrofitApiService
import com.gd.intership.hdziedziura.androidshopingapp.data.network.dto.ProductDto
import com.gd.intership.hdziedziura.androidshopingapp.domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: RetrofitApiService
) : ProductRepository {

    override suspend fun allProducts(): Response<List<ProductDto>> =
        withContext(Dispatchers.IO) {
            return@withContext api.allProducts()
        }

    override suspend fun allCategories(): Response<List<String>> =
        withContext(Dispatchers.IO) {
            return@withContext api.allCategories()
        }

    override suspend fun categoryProducts(category: String): Response<List<ProductDto>> =
        withContext(Dispatchers.IO) {
            return@withContext api.categoryProducts(category)
        }
}
