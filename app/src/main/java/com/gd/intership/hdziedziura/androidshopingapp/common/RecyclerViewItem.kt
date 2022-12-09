package com.gd.intership.hdziedziura.androidshopingapp.common

import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.item_vm.ItemVm

data class RecyclerViewItem(val layoutId: Int, val itemViewModel: ItemVm<*>) {
    val viewType = getViewTypeId()

    private fun getViewTypeId(): Int {
        var result = 31
        result = 31 * result + layoutId
        return result
    }
}
