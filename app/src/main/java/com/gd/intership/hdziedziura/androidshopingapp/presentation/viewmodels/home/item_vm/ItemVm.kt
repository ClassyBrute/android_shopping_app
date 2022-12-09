package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.item_vm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

const val NO_POSITION = -1

abstract class ItemVm<T> {
    abstract val item: T
    val itemsScope: CoroutineScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
    var position: Int = NO_POSITION
}
