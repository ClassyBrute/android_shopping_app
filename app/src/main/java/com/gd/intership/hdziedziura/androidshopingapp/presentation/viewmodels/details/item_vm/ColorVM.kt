package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.details.item_vm

import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.common.ItemWithEvent
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.shopRvItems.ColorItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.details.ProductDetailsViewModel
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.item_vm.ItemVm
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

data class ColorVM(
    override val item: ColorItem
) : ItemVm<ColorItem>(), ItemWithEvent<ProductDetailsViewModel.Event> {
    private val events: MutableSharedFlow<ProductDetailsViewModel.Event> = MutableSharedFlow()

    fun onColorClick() {
        itemsScope.launch {
            events.emit(ProductDetailsViewModel.Event.ColorClickEvent(item, position))
        }
    }

    override fun events(): SharedFlow<ProductDetailsViewModel.Event> {
        return events
    }
}

fun ColorVM.toRecyclerViewItem() = RecyclerViewItem(R.layout.item_details_color, this)
