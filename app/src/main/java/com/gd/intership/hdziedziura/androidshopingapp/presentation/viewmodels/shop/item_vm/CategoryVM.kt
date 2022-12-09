package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.item_vm

import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.common.ItemWithEvent
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.shopRvItems.CategoryItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.item_vm.ItemVm
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.ShopViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

data class CategoryVM(
    override val item: CategoryItem
) : ItemVm<CategoryItem>(), ItemWithEvent<ShopViewModel.Event> {
    private val events: MutableSharedFlow<ShopViewModel.Event> = MutableSharedFlow()

    fun onCategoryClick() {
        itemsScope.launch {
            events.emit(ShopViewModel.Event.CategoryClickEvent(item, position))
        }
    }

    override fun events(): SharedFlow<ShopViewModel.Event> {
        return events
    }
}

fun CategoryVM.toRecyclerViewItem() = RecyclerViewItem(R.layout.item_shop_category_image, this)
