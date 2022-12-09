package com.gd.intership.hdziedziura.androidshopingapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {
    @Query("SELECT * FROM product_entity ORDER BY id DESC")
    fun getAllProducts(): List<ProductEntity>?

    @Query("SELECT * FROM product_entity WHERE title = :title AND color = :color AND size = :size")
    fun getItem(title: String, color: String, size: String): List<ProductEntity>

    @Query("UPDATE product_entity SET count = count + 1 WHERE title = :title AND color = :color AND size = :size")
    fun increaseAmount(title: String, color: String, size: String)

    @Query("UPDATE product_entity SET count = count - 1 WHERE title = :title AND color = :color AND size = :size")
    fun decreaseAmount(title: String, color: String, size: String)

    @Query("DELETE FROM product_entity WHERE title = :title AND color = :color AND size = :size")
    fun deleteProduct(title: String, color: String, size: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(productEntity: ProductEntity)

    @Query("SELECT * FROM product_favorite_entity ORDER BY id DESC")
    fun getAllFavoriteProducts(): List<ProductFavoriteEntity>

    @Query("SELECT * FROM product_favorite_entity WHERE title = :title AND color = :color AND size = :size")
    fun getFavoriteItem(title: String, color: String, size: String): List<ProductFavoriteEntity>

    @Query("DELETE FROM product_favorite_entity WHERE title = :title AND color = :color AND size = :size")
    fun deleteFavoriteProduct(title: String, color: String, size: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteProduct(productFavoriteEntity: ProductFavoriteEntity)

    fun insertOrUpdate(item: ProductEntity) {
        val items = getItem(item.title, item.color, item.size)
        if (items.isEmpty()) insertProduct(item)
        else increaseAmount(item.title, item.color, item.size)
    }

    fun decrease(item: ProductEntity) {
        decreaseAmount(item.title, item.color, item.size)
    }

    fun increase(item: ProductEntity) {
        increaseAmount(item.title, item.color, item.size)
    }

    fun delete(item: ProductEntity) {
        deleteProduct(item.title, item.color, item.size)
    }

    fun deleteFavorite(item: ProductFavoriteEntity) {
        deleteFavoriteProduct(item.title, item.color, item.size)
    }

    fun insertOrDeleteFavorite(item: ProductFavoriteEntity) {
        val items = getFavoriteItem(item.title, item.color, item.size)
        if (items.isEmpty()) insertFavoriteProduct(item)
        else deleteFavorite(item)
    }
}
