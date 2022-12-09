package com.gd.intership.hdziedziura.androidshopingapp.data.mocks

import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.data.network.dto.ProductDto

val mockColorsHex = mapOf(
    "#FF0000" to "Red",
    "#3700B3" to "Dark Blue",
    "#0103FF" to "Blue",
    "#FFDAC5" to "Silk",
    "#018786" to "Cyan",
    "#DB3022" to "Vermilion",
    "#FFFFBB" to "Yellow",
    "#9B9B9B" to "Gray",
    "#FFBA49" to "Orange",
    "#2AA952" to "Green"
)

val mockSize = listOf(
    "XS",
    "S",
    "M",
    "L",
    "XL"
)

val mockBrand = listOf(
    "Ralph Lauren",
    "H&M",
    "Gucci",
    "Nike",
    "Zara",
    "Vans"
)

val mockReview = mutableListOf(
    ProductDto.ReviewDto(
        "Helene Moore",
        R.drawable.image_top,
        4,
        "June 5, 2019",
        "The dress is great! Very classy and comfortable. It fit perfectly! I'm 5'7 and 130 pounds. I am a 34B chest. This dress would be too long for those who are shorter but could be hemmed. I wouldn't recommend it for those big chested as I am smaller chested and it fit me perfectly. The underarms were not too wide and the dress was made well.",
        listOf(R.drawable.image_right, R.drawable.image_bottom),
    ),
    ProductDto.ReviewDto(
        "Kim Shine",
        R.drawable.image_bottom,
        5,
        "August 13, 2021",
        "I loved this dress so much as soon as I tried it on I knew I had to buy it in another color. I am 5'3 about 155lbs and I carry all my weight in my upper body. When I put it on I felt like it thinned me put and I got so many compliments.",
        listOf("")
    )
)
