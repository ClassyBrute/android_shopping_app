package com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product

import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.data.toProduct
import com.gd.intership.hdziedziura.androidshopingapp.domain.model.Product
import com.gd.intership.hdziedziura.androidshopingapp.domain.repository.ProductRepository
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import javax.inject.Inject

class ProductsAllUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository
) : ProductsAllUseCase {

    override suspend fun execute(): Resource<List<Product>> {
        return try {
            coroutineScope {
                val result = productRepository.allProducts().body()
                Resource.Success(
                    result?.map { it.toProduct() } ?: emptyList()
                )
            }
        } catch (e: IOException) {
            Resource.Error("Connection Error")
        }
    }
}
