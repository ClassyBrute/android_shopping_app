package com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product

import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.domain.repository.ProductRepository
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import javax.inject.Inject

class CategoriesAllUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository
) : CategoriesAllUseCase {

    override suspend fun execute(): Resource<List<String>> {
        return try {
            coroutineScope {
                val result = productRepository.allCategories().body()
                Resource.Success(result ?: emptyList())
            }
        } catch (e: IOException) {
            Resource.Error("connection error")
        }
    }
}
