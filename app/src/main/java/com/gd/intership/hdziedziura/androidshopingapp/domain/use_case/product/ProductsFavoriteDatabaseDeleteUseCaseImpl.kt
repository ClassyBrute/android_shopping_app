package com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product

import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.data.toProductFavoriteEntity
import com.gd.intership.hdziedziura.androidshopingapp.domain.model.Product
import com.gd.intership.hdziedziura.androidshopingapp.domain.repository.DatabaseRepository
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import javax.inject.Inject

class ProductsFavoriteDatabaseDeleteUseCaseImpl @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ProductsFavoriteDatabaseDeleteUseCase {

    override suspend fun execute(product: Product): Resource<String> {
        return try {
            coroutineScope {
                databaseRepository.deleteFavoriteProduct(product.toProductFavoriteEntity())
                Resource.Success("Successfully deleted from database")
            }
        } catch (e: IOException) {
            Resource.Error("Couldn't delete from database")
        }
    }
}
