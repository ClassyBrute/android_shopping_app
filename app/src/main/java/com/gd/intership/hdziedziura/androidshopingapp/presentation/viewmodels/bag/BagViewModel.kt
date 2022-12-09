package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.bag

import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.data.toProduct
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsDatabaseAllUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsDatabaseDecreaseUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsDatabaseDeleteUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsDatabaseIncreaseUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsFavoriteDatabaseAllUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsFavoriteDatabaseDeleteUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsFavoriteDatabaseInsertUseCase
import com.gd.intership.hdziedziura.androidshopingapp.presentation.homeRvItems.ProductItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.bag.item_vm.ProductVM
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.bag.item_vm.toRecyclerViewItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class BagViewModel @Inject constructor(
    private val databaseAllUseCase: ProductsDatabaseAllUseCase,
    private val databaseIncreaseUseCase: ProductsDatabaseIncreaseUseCase,
    private val databaseDecreaseUseCase: ProductsDatabaseDecreaseUseCase,
    private val databaseDeleteUseCase: ProductsDatabaseDeleteUseCase,
    private val favoriteDatabaseInsertUseCase: ProductsFavoriteDatabaseInsertUseCase,
    private val favoriteDatabaseAllUseCase: ProductsFavoriteDatabaseAllUseCase,
    private val favoriteDatabaseDeleteUseCase: ProductsFavoriteDatabaseDeleteUseCase
) : ViewModel(), DefaultLifecycleObserver {
    private val events = MutableSharedFlow<Event>()
    val uiEvents: SharedFlow<Event> = events
    val data: ObservableField<List<RecyclerViewItem>> = ObservableField(emptyList())
    private var productItemsList: List<ProductItem> = mutableListOf()
    val totalAmount: ObservableField<String> = ObservableField("")

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        viewModelScope.launch {
            fetchProducts()
        }.invokeOnCompletion {
            createRecyclerItems()
            calculateTotal()
        }
    }

    fun deleteFromDb(item: ProductItem) {
        viewModelScope.launch {
            when (databaseDeleteUseCase.execute(item.toProduct())) {
                is Resource.Success -> {
                    events.emit(Event.DeleteSuccessEvent)
                    fetchProducts()
                }
                is Resource.Error -> events.emit(Event.DeleteFailEvent)
                else -> {}
            }
        }.invokeOnCompletion {
            createRecyclerItems()
            calculateTotal()
        }
    }

    fun increaseCount(item: ProductItem) {
        viewModelScope.launch {
            when (databaseIncreaseUseCase.execute(item.toProduct())) {
                is Resource.Success -> {
                    events.emit(Event.ChangeAmountSuccessEvent)
                    fetchProducts()
                }
                is Resource.Error -> events.emit(Event.ChangeAmountErrorEvent)
                else -> {}
            }
        }.invokeOnCompletion {
            createRecyclerItems()
            calculateTotal()
        }
    }

    fun decreaseCount(item: ProductItem) {
        viewModelScope.launch {
            when (databaseDecreaseUseCase.execute(item.toProduct())) {
                is Resource.Success -> {
                    events.emit(Event.ChangeAmountSuccessEvent)
                    fetchProducts()
                }
                is Resource.Error -> events.emit(Event.ChangeAmountErrorEvent)
                else -> {}
            }
        }.invokeOnCompletion {
            createRecyclerItems()
            calculateTotal()
        }
    }

    private fun calculateTotal() {
        var total = 0f
        productItemsList.forEach {
            total += it.count.get() * it.price.toFloat()
        }
        totalAmount.set(String.format("%.2f", total))
    }

    fun favoritesAdd(item: ProductItem) {
        viewModelScope.launch {
            favoriteDatabaseInsertUseCase.execute(item.toProduct())
            fetchProducts()
        }.invokeOnCompletion {
            createRecyclerItems()
        }
    }

    fun favoritesRemove(item: ProductItem) {
        viewModelScope.launch {
            favoriteDatabaseDeleteUseCase.execute(item.toProduct())
            fetchProducts()
        }.invokeOnCompletion {
            createRecyclerItems()
        }
    }

    private fun createRecyclerItems() {
        val newList = mutableListOf<RecyclerViewItem>()

        productItemsList.forEach { product ->
            newList.add(
                ProductVM(
                    product
                ).let {
                    viewModelScope.launch {
                        it.events().collectLatest { event ->
                            this@BagViewModel.events.emit(event)
                        }
                    }
                    it.toRecyclerViewItem()
                }
            )
        }
        data.set(newList)
    }

    private suspend fun fetchProducts() {
        when (val result = databaseAllUseCase.execute()) {
            is Resource.Success -> {
                val favorites = favoriteDatabaseAllUseCase.execute()
                result.data?.apply {
                    productItemsList = this.map {
                        ProductItem(
                            title = it.title,
                            brand = it.brand,
                            price = it.price,
                            category = it.category,
                            rating = it.rating.rate,
                            reviewCount = it.rating.count,
                            image = it.image,
                            isNew = true,
                            count = ObservableInt(it.count),
                            color = it.colorHex,
                            colorText = it.colorText,
                            size = it.size,
                            isFavorite = favorites.data?.any { product ->
                                it.title == product.title &&
                                    it.colorText[0] == product.chosenColor &&
                                    it.size[0] == product.chosenSize
                            } == true,
                            chosenColor = it.colorText[0],
                            chosenSize = it.size[0]
                        )
                    }
                }
            }
            is Resource.Error -> {
                events.emit(Event.FetchingErrorEvent)
            }
            else -> {}
        }
    }

    sealed class Event {
        object FetchingErrorEvent : Event()
        object DeleteSuccessEvent : Event()
        object DeleteFailEvent : Event()
        object ChangeAmountSuccessEvent : Event()
        object ChangeAmountErrorEvent : Event()
        class MoreEvent(val item: ProductItem, val view: View) : Event()
        class ProductClickEvent(val item: ProductItem, val position: Int) : Event()
        class IncreaseEvent(val item: ProductItem, val position: Int) : Event()
        class DecreaseEvent(val item: ProductItem, val position: Int) : Event()
    }
}
