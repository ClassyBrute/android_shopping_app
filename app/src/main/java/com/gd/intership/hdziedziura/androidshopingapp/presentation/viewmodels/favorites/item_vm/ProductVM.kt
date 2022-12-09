package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.favorites.item_vm

import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.common.ItemWithEvent
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.homeRvItems.ProductItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.favorites.FavoritesViewModel
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.item_vm.ItemVm
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

data class ProductVM(
    override val item: ProductItem
) : ItemVm<ProductItem>(), ItemWithEvent<FavoritesViewModel.Event> {
    private val events: MutableSharedFlow<FavoritesViewModel.Event> = MutableSharedFlow()

    fun onProductClick() {
        itemsScope.launch {
            events.emit(FavoritesViewModel.Event.ProductClickEvent(item, position))
        }
    }

    fun onDeleteClick() {
        itemsScope.launch {
            events.emit(FavoritesViewModel.Event.DeleteEvent(item, position))
        }
    }

    fun onAddToCartClick() {
        itemsScope.launch {
            events.emit(FavoritesViewModel.Event.AddToCartEvent(item, position))
        }
    }

    override fun events(): SharedFlow<FavoritesViewModel.Event> {
        return events
    }
}

fun ProductVM.toRecyclerViewItemList() = RecyclerViewItem(R.layout.item_favorite_product_list, this)
fun ProductVM.toRecyclerViewItemGrid() = RecyclerViewItem(R.layout.item_favorite_product_grid, this)
