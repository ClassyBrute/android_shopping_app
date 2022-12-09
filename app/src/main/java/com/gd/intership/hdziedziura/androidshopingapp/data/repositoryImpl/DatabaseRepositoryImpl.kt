package com.gd.intership.hdziedziura.androidshopingapp.data.repositoryImpl

import com.gd.intership.hdziedziura.androidshopingapp.data.database.AppDatabase
import com.gd.intership.hdziedziura.androidshopingapp.data.database.ProductDao
import com.gd.intership.hdziedziura.androidshopingapp.data.database.ProductEntity
import com.gd.intership.hdziedziura.androidshopingapp.data.database.ProductFavoriteEntity
import com.gd.intership.hdziedziura.androidshopingapp.domain.repository.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    database: AppDatabase
) : DatabaseRepository {
    private val productDao: ProductDao = database.getProductDao()

    override suspend fun getProducts(): List<ProductEntity> =
        withContext(Dispatchers.IO) {
            val products = productDao.getAllProducts()
            if (products?.isNotEmpty() == true) {
                return@withContext products
            } else {
                return@withContext listOf()
            }
        }

    override suspend fun insertOrUpdateProduct(productEntity: ProductEntity) =
        withContext(Dispatchers.IO) {
            return@withContext productDao.insertOrUpdate(productEntity)
        }

    override suspend fun decreaseAmount(productEntity: ProductEntity) =
        withContext(Dispatchers.IO) {
            return@withContext productDao.decrease(productEntity)
        }

    override suspend fun increaseAmount(productEntity: ProductEntity) =
        withContext(Dispatchers.IO) {
            return@withContext productDao.increase(productEntity)
        }

    override suspend fun deleteProduct(productEntity: ProductEntity) =
        withContext(Dispatchers.IO) {
            return@withContext productDao.delete(productEntity)
        }

    override suspend fun getFavoriteProducts(): List<ProductFavoriteEntity> =
        withContext(Dispatchers.IO) {
            return@withContext productDao.getAllFavoriteProducts()
        }

    override suspend fun insertFavoriteProduct(productFavoriteEntity: ProductFavoriteEntity) =
        withContext(Dispatchers.IO) {
            return@withContext productDao.insertOrDeleteFavorite(productFavoriteEntity)
        }

    override suspend fun deleteFavoriteProduct(productFavoriteEntity: ProductFavoriteEntity) =
        withContext(Dispatchers.IO) {
            return@withContext productDao.deleteFavorite(productFavoriteEntity)
        }
}
