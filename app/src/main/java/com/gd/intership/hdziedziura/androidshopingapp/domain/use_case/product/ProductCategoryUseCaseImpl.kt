package com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product

import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.data.toProduct
import com.gd.intership.hdziedziura.androidshopingapp.domain.model.Product
import com.gd.intership.hdziedziura.androidshopingapp.domain.repository.ProductRepository
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import javax.inject.Inject

class ProductCategoryUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository
) : ProductCategoryUseCase {

    override suspend fun execute(category: String): Resource<List<Product>> {
        return try {
            coroutineScope {
                val result = productRepository.categoryProducts(category).body()
                Resource.Success(
                    result?.map { it.toProduct() } ?: emptyList()
                )
            }
        } catch (e: IOException) {
            Resource.Error("connection error")
        }
    }
}
