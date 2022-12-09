package com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product

import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.data.toProductEntity
import com.gd.intership.hdziedziura.androidshopingapp.domain.model.Product
import com.gd.intership.hdziedziura.androidshopingapp.domain.repository.DatabaseRepository
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import javax.inject.Inject

class ProductsDatabaseIncreaseUseCaseImpl @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ProductsDatabaseIncreaseUseCase {

    override suspend fun execute(product: Product): Resource<String> {
        return try {
            coroutineScope {
                databaseRepository.increaseAmount(product.toProductEntity())
                Resource.Success("Successfully increased amount")
            }
        } catch (e: IOException) {
            Resource.Error("Couldn't increase in database")
        }
    }
}
