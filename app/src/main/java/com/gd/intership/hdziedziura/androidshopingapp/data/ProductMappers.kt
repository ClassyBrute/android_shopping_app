package com.gd.intership.hdziedziura.androidshopingapp.data

import com.gd.intership.hdziedziura.androidshopingapp.data.database.ProductEntity
import com.gd.intership.hdziedziura.androidshopingapp.data.database.ProductFavoriteEntity
import com.gd.intership.hdziedziura.androidshopingapp.data.network.dto.ProductDto
import com.gd.intership.hdziedziura.androidshopingapp.domain.model.Product

fun ProductDto.toProduct() =
    Product(
        id, title, price, category,
        Product.Rate(
            rating.rate, rating.count, rating.five,
            rating.four, rating.three, rating.two, rating.one
        ),
        description, image, brand, color,
        colorText, colorHex, size, count,
        reviews.mapTo(mutableListOf()) {
            Product.Review(
                it.name, it.picture, it.rating,
                it.date, it.text, it.images
            )
        }
    )

fun Product.toProductDto() =
    ProductDto(
        id, title, price, category,
        ProductDto.RateDto(
            rating.rate, rating.count, rating.five,
            rating.four, rating.three, rating.two, rating.one
        ),
        description, image, brand, color,
        colorText, colorHex, count, size,
        reviews.mapTo(mutableListOf()) {
            ProductDto.ReviewDto(
                it.name, it.picture, it.rating,
                it.date, it.text, it.images
            )
        }
    )

fun ProductEntity.toProduct() =
    Product(
        id = id,
        title = title,
        price = price,
        image = image,
        colorText = listOf(color),
        size = listOf(size),
        count = count,
        category = category,
        brand = brand,
        rating = Product.Rate(
            rating,
            reviewCount
        )
    )

fun Product.toProductEntity() =
    ProductEntity(
        id = id,
        title = title,
        price = price,
        image = image,
        color = colorText[0],
        size = size[0],
        count = count,
        category = category,
        brand = brand,
        rating = rating.rate,
        reviewCount = rating.count
    )

fun ProductFavoriteEntity.toProduct() =
    Product(
        id = id,
        title = title,
        price = price,
        image = image,
        colorText = listOf(color),
        size = listOf(size),
        count = count,
        category = category,
        chosenColor = color,
        chosenSize = size,
        brand = brand,
        rating = Product.Rate(
            rating,
            reviewCount
        )
    )

fun Product.toProductFavoriteEntity() =
    ProductFavoriteEntity(
        id = id,
        title = title,
        price = price,
        image = image,
        color = chosenColor,
        size = chosenSize,
        count = count,
        category = category,
        brand = brand,
        rating = rating.rate,
        reviewCount = rating.count
    )

fun com.gd.intership.hdziedziura.androidshopingapp.presentation.homeRvItems.ProductItem.toProduct() =
    Product(
        title = title,
        price = price,
        image = image,
        colorText = colorText,
        size = size,
        category = category,
        chosenColor = chosenColor,
        chosenSize = chosenSize,
        brand = brand,
        rating = Product.Rate(
            rating,
            reviewCount
        )
    )

fun com.gd.intership.hdziedziura.androidshopingapp.presentation.shopRvItems.ProductItem.toProduct() =
    Product(
        title = title,
        price = price,
        image = image,
        colorText = colorText,
        size = size,
        category = category,
        chosenColor = chosenColor,
        chosenSize = chosenSize,
        brand = brand,
        rating = Product.Rate(
            rating,
            reviewCount
        )
    )
