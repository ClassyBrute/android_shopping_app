package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.bag.item_vm

import android.view.View
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.common.ItemWithEvent
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.homeRvItems.ProductItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.bag.BagViewModel
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.item_vm.ItemVm
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

data class ProductVM(
    override val item: ProductItem
) : ItemVm<ProductItem>(), ItemWithEvent<BagViewModel.Event> {
    private val events: MutableSharedFlow<BagViewModel.Event> = MutableSharedFlow()

    fun onProductClick() {
        itemsScope.launch {
            events.emit(BagViewModel.Event.ProductClickEvent(item, position))
        }
    }

    fun onIncreaseClick() {
        itemsScope.launch {
            events.emit(BagViewModel.Event.IncreaseEvent(item, position))
        }
    }

    fun onDecreaseClick() {
        itemsScope.launch {
            events.emit(BagViewModel.Event.DecreaseEvent(item, position))
        }
    }

    fun onMoreClick(view: View) {
        itemsScope.launch {
            events.emit(BagViewModel.Event.MoreEvent(item, view))
        }
    }

    override fun events(): SharedFlow<BagViewModel.Event> {
        return events
    }
}

fun ProductVM.toRecyclerViewItem() = RecyclerViewItem(R.layout.item_bag_product, this)
