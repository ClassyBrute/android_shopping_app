package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.item_vm

import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.common.ItemWithEvent
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.shopRvItems.BannerItemShop
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.item_vm.ItemVm
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.ShopViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

data class BannerShopVM(
    override val item: BannerItemShop
) : ItemVm<BannerItemShop>(), ItemWithEvent<ShopViewModel.Event> {
    private var events: MutableSharedFlow<ShopViewModel.Event> = MutableSharedFlow()

    fun onBannerClick() {
        itemsScope.launch {
            events.emit(ShopViewModel.Event.BannerClickEvent(item, position))
        }
    }

    override fun events(): SharedFlow<ShopViewModel.Event> {
        return events
    }
}

fun BannerShopVM.toRecyclerView() = RecyclerViewItem(R.layout.item_shop_category_banner, this)
