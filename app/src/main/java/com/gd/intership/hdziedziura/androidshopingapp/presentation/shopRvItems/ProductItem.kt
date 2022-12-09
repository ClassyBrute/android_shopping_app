package com.gd.intership.hdziedziura.androidshopingapp.presentation.shopRvItems

import androidx.databinding.ObservableInt
import com.gd.intership.hdziedziura.androidshopingapp.domain.model.Product

data class ProductItem(
    val title: String = "",
    val brand: String = "",
    val isSale: Boolean = false,
    val isNew: Boolean = false,
    val oldPrice: String = "0",
    val price: String = "0",
    val saleAmount: String = "0%",
    val image: String = "",
    val category: String = "",
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val five: Int = 0,
    val four: Int = 0,
    val three: Int = 0,
    val two: Int = 0,
    val one: Int = 0,
    var count: ObservableInt = ObservableInt(0),
    val color: List<String> = emptyList(),
    val colorText: List<String> = emptyList(),
    val size: List<String> = emptyList(),
    val description: String = "",
    val reviews: MutableList<Product.Review> = mutableListOf(),
    var isFavorite: Boolean = false,
    var chosenColor: String = "",
    var chosenSize: String = ""
)
