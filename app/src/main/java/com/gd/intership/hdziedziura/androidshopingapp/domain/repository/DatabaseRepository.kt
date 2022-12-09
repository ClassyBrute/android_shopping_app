package com.gd.intership.hdziedziura.androidshopingapp.domain.repository

import com.gd.intership.hdziedziura.androidshopingapp.data.database.ProductEntity
import com.gd.intership.hdziedziura.androidshopingapp.data.database.ProductFavoriteEntity

interface DatabaseRepository {
    suspend fun getProducts(): List<ProductEntity>
    suspend fun insertOrUpdateProduct(productEntity: ProductEntity)
    suspend fun decreaseAmount(productEntity: ProductEntity)
    suspend fun increaseAmount(productEntity: ProductEntity)
    suspend fun deleteProduct(productEntity: ProductEntity)
    suspend fun getFavoriteProducts(): List<ProductFavoriteEntity>
    suspend fun insertFavoriteProduct(productFavoriteEntity: ProductFavoriteEntity)
    suspend fun deleteFavoriteProduct(productFavoriteEntity: ProductFavoriteEntity)
}
