package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.item_vm

import android.view.View
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.common.ItemWithEvent
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.shopRvItems.ProductItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.item_vm.ItemVm
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.ShopProductListViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

data class ProductVM(
    override val item: ProductItem
) : ItemVm<ProductItem>(), ItemWithEvent<ShopProductListViewModel.Event> {
    private val events: MutableSharedFlow<ShopProductListViewModel.Event> = MutableSharedFlow()

    fun onProductClick() {
        itemsScope.launch {
            events.emit(ShopProductListViewModel.Event.ProductClickEvent(item, position))
        }
    }

    fun onFavoriteClick(view: View) {
        itemsScope.launch {
            events.emit(ShopProductListViewModel.Event.FavoriteClickEvent(item, position, view))
        }
    }

    override fun events(): SharedFlow<ShopProductListViewModel.Event> {
        return events
    }
}

fun ProductVM.toRecyclerViewItemList() = RecyclerViewItem(R.layout.item_shop_product_list, this)
fun ProductVM.toRecyclerViewItemGrid() = RecyclerViewItem(R.layout.item_shop_product_grid, this)
