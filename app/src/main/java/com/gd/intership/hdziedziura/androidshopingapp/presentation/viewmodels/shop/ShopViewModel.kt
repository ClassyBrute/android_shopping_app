package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop

import androidx.databinding.ObservableField
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gd.intership.hdziedziura.androidshopingapp.presentation.shopRvItems.BannerItemShop
import com.gd.intership.hdziedziura.androidshopingapp.presentation.shopRvItems.CategoryItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.adapter.ViewPagerAdapter
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopViewModel @Inject constructor() : ViewModel(), DefaultLifecycleObserver {
    private val events = MutableSharedFlow<Event>()
    val adapter: ObservableField<ViewPagerAdapter> = ObservableField()
    val uiEvents: SharedFlow<Event> = events

    fun emitInnerEvents(event: Event) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

    sealed class Event {
        object FetchingErrorEvent : Event()
        class BannerClickEvent(val item: BannerItemShop, val position: Int) : Event()
        class CategoryClickEvent(val item: CategoryItem, val position: Int) : Event()
    }
}
