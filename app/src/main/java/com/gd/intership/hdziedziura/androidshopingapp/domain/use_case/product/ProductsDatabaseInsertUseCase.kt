package com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product

import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.domain.model.Product

interface ProductsDatabaseInsertUseCase {
    suspend fun execute(product: Product): Resource<String>
}
