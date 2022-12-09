package com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product

import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.data.toProductEntity
import com.gd.intership.hdziedziura.androidshopingapp.domain.model.Product
import com.gd.intership.hdziedziura.androidshopingapp.domain.repository.DatabaseRepository
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import javax.inject.Inject

class ProductsDatabaseDeleteUseCaseImpl @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ProductsDatabaseDeleteUseCase {

    override suspend fun execute(product: Product): Resource<String> {
        return try {
            coroutineScope {
                databaseRepository.deleteProduct(product.toProductEntity())
                Resource.Success("Successfully deleted from database")
            }
        } catch (e: IOException) {
            Resource.Error("Couldn't delete from database")
        }
    }
}
