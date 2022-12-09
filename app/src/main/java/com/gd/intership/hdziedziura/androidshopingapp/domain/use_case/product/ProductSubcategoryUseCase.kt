package com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product

import com.gd.intership.hdziedziura.androidshopingapp.common.Resource

interface ProductSubcategoryUseCase {
    suspend fun execute(category: String): Resource<Map<String, Boolean>>
}
