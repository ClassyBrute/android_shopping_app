package com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product

import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.data.toProduct
import com.gd.intership.hdziedziura.androidshopingapp.domain.model.Product
import com.gd.intership.hdziedziura.androidshopingapp.domain.repository.DatabaseRepository
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import javax.inject.Inject

class ProductsDatabaseAllUseCaseImpl @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ProductsDatabaseAllUseCase {

    override suspend fun execute(): Resource<List<Product>> {
        return try {
            coroutineScope {
                val result = databaseRepository.getProducts()
                Resource.Success(
                    result.map { it.toProduct() }
                )
            }
        } catch (e: IOException) {
            Resource.Error("Couldn't receive from DB")
        }
    }
}
