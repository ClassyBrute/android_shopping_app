package com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product

import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.domain.model.Product

interface ProductsDatabaseAllUseCase {
    suspend fun execute(): Resource<List<Product>>
}
