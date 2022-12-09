package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.item_vm

import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.common.ItemWithEvent
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.shopRvItems.SizeItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.item_vm.ItemVm
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.ShopProductListViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

data class SizeVM(
    override val item: SizeItem
) : ItemVm<SizeItem>(), ItemWithEvent<ShopProductListViewModel.Event> {
    private val events: MutableSharedFlow<ShopProductListViewModel.Event> = MutableSharedFlow()

    fun onSizeClick() {
        item.checked.set(!item.checked.get())
        itemsScope.launch {
            events.emit(ShopProductListViewModel.Event.SizeClickEvent(item, position))
        }
    }

    override fun events(): SharedFlow<ShopProductListViewModel.Event> {
        return events
    }
}

fun SizeVM.toRecyclerViewItem() = RecyclerViewItem(R.layout.item_shop_filters_size, this)
fun SizeVM.toRecyclerViewItemFav() = RecyclerViewItem(R.layout.item_shop_fav_size, this)
