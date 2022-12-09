package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.item_vm

import android.view.View
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.common.ItemWithEvent
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.homeRvItems.ProductItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.HomeViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

data class ProductVM(
    override var item: ProductItem
) : ItemVm<ProductItem>(), ItemWithEvent<HomeViewModel.Event> {
    private val events: MutableSharedFlow<HomeViewModel.Event> = MutableSharedFlow()

    fun onProductClick() {
        itemsScope.launch {
            events.emit(HomeViewModel.Event.ProductClickEvent(item, position))
        }
    }

    fun onFavoriteClick(view: View) {
        itemsScope.launch {
            events.emit(HomeViewModel.Event.FavoriteClickEvent(item, position, view))
        }
    }

    override fun events(): SharedFlow<HomeViewModel.Event> {
        return events
    }
}

fun ProductVM.toRecyclerViewItem() = RecyclerViewItem(R.layout.item_home_product, this)
