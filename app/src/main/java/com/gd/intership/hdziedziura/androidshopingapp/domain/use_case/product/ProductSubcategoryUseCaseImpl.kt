package com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product

import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.domain.repository.ProductRepository
import kotlinx.coroutines.coroutineScope
import okio.IOException
import javax.inject.Inject

class ProductSubcategoryUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository
) : ProductSubcategoryUseCase {

    override suspend fun execute(category: String): Resource<Map<String, Boolean>> {
        return try {
            coroutineScope {
                val result = when (category.lowercase()) {
                    "women's clothing" -> mapOf(
                        "Jacket" to true,
                        "Short Sleeve" to true,
                        "T Shirt" to true
                    )
                    "men's clothing" -> mapOf(
                        "Backpack" to true,
                        "T-Shirt" to true,
                        "Jacket" to true
                    )
                    "jewelery" -> mapOf(
                        "Bracelet" to true,
                        "Micropave" to true,
                        "Gold" to true,
                        "Princess" to true
                    )
                    "electronics" -> mapOf(
                        "Hard Drive" to true,
                        "SSD" to true,
                        "Acer" to true,
                        "Samsung" to true
                    )
                    else -> mapOf()
                }
                Resource.Success(result)
            }
        } catch (e: IOException) {
            Resource.Error("connection error")
        }
    }
}
