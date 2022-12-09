package com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product

import com.gd.intership.hdziedziura.androidshopingapp.common.Resource

interface CategoriesAllUseCase {
    suspend fun execute(): Resource<List<String>>
}
