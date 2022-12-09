package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.favorites.item_vm

import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.common.ItemWithEvent
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.shopRvItems.SortItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.favorites.FavoritesViewModel
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.item_vm.ItemVm
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

data class SortVM(
    override val item: SortItem
) : ItemVm<SortItem>(), ItemWithEvent<FavoritesViewModel.Event> {
    private val events: MutableSharedFlow<FavoritesViewModel.Event> = MutableSharedFlow()

    fun onSortClick() {
        itemsScope.launch {
            events.emit(FavoritesViewModel.Event.SortTypeClickEvent(item, position))
        }
    }

    override fun events(): SharedFlow<FavoritesViewModel.Event> {
        return events
    }
}

fun SortVM.toRecyclerViewItem() = RecyclerViewItem(R.layout.item_favorite_sort, this)
