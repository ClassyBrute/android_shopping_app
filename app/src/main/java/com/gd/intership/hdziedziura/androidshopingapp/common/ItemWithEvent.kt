package com.gd.intership.hdziedziura.androidshopingapp.common

import kotlinx.coroutines.flow.SharedFlow

interface ItemWithEvent<T : Any> {
    fun events(): SharedFlow<T>
}
