package com.gd.intership.hdziedziura.androidshopingapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_favorite_entity")
class ProductFavoriteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "price") val price: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "color") val color: String,
    @ColumnInfo(name = "size") val size: String,
    @ColumnInfo(name = "count") val count: Int = 1,
    @ColumnInfo(name = "category") val category: String = "",
    @ColumnInfo(name = "brand") val brand: String,
    @ColumnInfo(name = "rating") val rating: Float,
    @ColumnInfo(name = "reviewCount") val reviewCount: Int
)
