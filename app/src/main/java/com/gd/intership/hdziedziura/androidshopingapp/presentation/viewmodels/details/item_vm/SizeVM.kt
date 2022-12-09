package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.details.item_vm

import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.common.ItemWithEvent
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.shopRvItems.SizeItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.details.ProductDetailsViewModel
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.item_vm.ItemVm
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

data class SizeVM(
    override val item: SizeItem
) : ItemVm<SizeItem>(), ItemWithEvent<ProductDetailsViewModel.Event> {
    private val events: MutableSharedFlow<ProductDetailsViewModel.Event> = MutableSharedFlow()

    fun onSizeClick() {
        itemsScope.launch {
            events.emit(ProductDetailsViewModel.Event.SizeClickEvent(item, position))
        }
    }

    override fun events(): SharedFlow<ProductDetailsViewModel.Event> {
        return events
    }
}

fun SizeVM.toRecyclerViewItem() = RecyclerViewItem(R.layout.item_details_size, this)
