package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.item_vm

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.common.ItemWithEvent
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.homeRvItems.CarouselItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.HomeViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class CarouselVM(override var item: CarouselItem) : ItemVm<CarouselItem>(), ItemWithEvent<HomeViewModel.Event> {
    private val events = MutableSharedFlow<HomeViewModel.Event>()
    val data: ObservableList<RecyclerViewItem> = ObservableArrayList<RecyclerViewItem>().apply {
        addAll(
            item.productItems.map { productItem ->
                ProductVM(productItem)
                    .let {
                        itemsScope.launch {
                            it.events().collectLatest { productEvent ->
                                events.emit(productEvent)
                            }
                        }
                        it.toRecyclerViewItem()
                    }
            }
        )
    }

    fun onCategoryClick() {
        itemsScope.launch {
            events.emit(HomeViewModel.Event.CarouselClickEvent(item, position))
        }
    }

    override fun events(): SharedFlow<HomeViewModel.Event> {
        return events
    }
}

fun CarouselVM.toRecyclerViewItem() = RecyclerViewItem(R.layout.item_home_category, this)
