package com.gd.intership.hdziedziura.androidshopingapp.domain.model

data class Product(
    val id: Int = 0,
    val title: String = "",
    val price: String = "",
    val category: String = "",
    val rating: Rate = Rate(),
    val description: String = "",
    val image: String = "",
    val brand: String = "",
    val color: List<Map.Entry<String, String>> = emptyList(),
    val colorText: List<String> = emptyList(),
    val colorHex: List<String> = emptyList(),
    val size: List<String> = emptyList(),
    val count: Int = 1,
    val reviews: MutableList<Review> = mutableListOf(),
    val favorite: Boolean = false,
    val chosenColor: String = "",
    val chosenSize: String = ""
) {
    data class Rate(
        val rate: Float = 0f,
        val count: Int = 0,
        val five: Int = 0,
        val four: Int = 0,
        val three: Int = 0,
        val two: Int = 0,
        val one: Int = 0
    )

    data class Review(
        val name: String = "",
        val picture: Int = 0,
        val rating: Int = 0,
        val date: String = "",
        val text: String = "",
        val images: List<*>
    )
}
