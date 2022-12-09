package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.item_vm

import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.common.ItemWithEvent
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.homeRvItems.BannerItemHome
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.HomeViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

data class BannerBigVM(
    override val item: BannerItemHome,
) : ItemVm<BannerItemHome>(), ItemWithEvent<HomeViewModel.Event> {
    private var events: MutableSharedFlow<HomeViewModel.Event> = MutableSharedFlow()

    fun onTopClick() {
        itemsScope.launch {
            events.emit(HomeViewModel.Event.BannerBigTopClickEvent(item, position))
        }
    }

    fun onRightClick() {
        itemsScope.launch {
            events.emit(HomeViewModel.Event.BannerBigRightClickEvent(item, position))
        }
    }
    fun onLeftClick() {
        itemsScope.launch {
            events.emit(HomeViewModel.Event.BannerBigLeftClickEvent(item, position))
        }
    }
    fun onBottomClick() {
        itemsScope.launch {
            events.emit(HomeViewModel.Event.BannerBigBottomClickEvent(item, position))
        }
    }

    override fun events(): SharedFlow<HomeViewModel.Event> {
        return events
    }
}

fun BannerBigVM.toRecyclerViewItemBig() = RecyclerViewItem(R.layout.item_home_grid, this)
