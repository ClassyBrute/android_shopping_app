package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.item_vm

import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.common.ItemWithEvent
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.homeRvItems.BannerItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.HomeViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

data class BannerVM(
    override val item: BannerItem,
) : ItemVm<BannerItem>(), ItemWithEvent<HomeViewModel.Event> {
    private var events: MutableSharedFlow<HomeViewModel.Event> = MutableSharedFlow()

    fun onBannerClick() {
        itemsScope.launch {
            events.emit(HomeViewModel.Event.BannerClickEvent(item, position))
        }
    }

    override fun events(): SharedFlow<HomeViewModel.Event> {
        return events
    }
}

fun BannerVM.toRecyclerView() = RecyclerViewItem(R.layout.item_home_banner, this)
