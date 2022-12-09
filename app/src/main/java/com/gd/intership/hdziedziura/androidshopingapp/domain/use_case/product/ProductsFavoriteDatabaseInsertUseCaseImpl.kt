package com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product

import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.data.toProductFavoriteEntity
import com.gd.intership.hdziedziura.androidshopingapp.domain.model.Product
import com.gd.intership.hdziedziura.androidshopingapp.domain.repository.DatabaseRepository
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import javax.inject.Inject

class ProductsFavoriteDatabaseInsertUseCaseImpl @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ProductsFavoriteDatabaseInsertUseCase {

    override suspend fun execute(product: Product): Resource<String> {
        return try {
            coroutineScope {
                databaseRepository.insertFavoriteProduct(product.toProductFavoriteEntity())
                Resource.Success("Success saving")
            }
        } catch (e: IOException) {
            Resource.Error("Couldn't save to DB")
        }
    }
}
