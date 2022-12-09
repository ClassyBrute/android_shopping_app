package com.gd.intership.hdziedziura.androidshopingapp.data.network.dto

import com.gd.intership.hdziedziura.androidshopingapp.data.mocks.mockBrand
import com.gd.intership.hdziedziura.androidshopingapp.data.mocks.mockColorsHex
import com.gd.intership.hdziedziura.androidshopingapp.data.mocks.mockReview
import com.gd.intership.hdziedziura.androidshopingapp.data.mocks.mockSize
import kotlin.random.Random

data class ProductDto(
    val id: Int = 0,
    val title: String = "",
    val price: String = "",
    val category: String = "",
    val rating: RateDto = RateDto(),
    val description: String = "",
    val image: String = "",
    val brand: String = mockBrand.shuffled()[0],
    val color: List<Map.Entry<String, String>> = mockColorsHex.entries.shuffled().take(Random.nextInt(2, 5)),
    val colorText: List<String> = color.map { it.value },
    val colorHex: List<String> = color.map { it.key },
    val count: Int = 1,
    val size: List<String> = mockSize.shuffled().take(Random.nextInt(2, 5)),
    val reviews: MutableList<ReviewDto> = mockReview
) {
    data class RateDto(
        val rate: Float = 0f,
        val count: Int = 0,
        val five: Int = Random.nextInt(5, 20),
        val four: Int = Random.nextInt(5, 20),
        val three: Int = Random.nextInt(5, 20),
        val two: Int = Random.nextInt(5, 20),
        val one: Int = Random.nextInt(5, 20)
    )

    data class ReviewDto(
        val name: String = "",
        val picture: Int = 0,
        val rating: Int = 0,
        val date: String = "",
        val text: String = "",
        val images: List<*>
    )
}
