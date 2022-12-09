package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.favorites

import androidx.databinding.ObservableField
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.data.toProduct
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsDatabaseInsertUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsFavoriteDatabaseAllUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsFavoriteDatabaseDeleteUseCase
import com.gd.intership.hdziedziura.androidshopingapp.presentation.homeRvItems.ProductItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.shopRvItems.SortItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.SortColorPreferences
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.favorites.item_vm.ProductVM
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.favorites.item_vm.SortVM
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.favorites.item_vm.toRecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.favorites.item_vm.toRecyclerViewItemGrid
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.favorites.item_vm.toRecyclerViewItemList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.internal.immutableListOf
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(
    private val favoriteDatabaseAllUseCase: ProductsFavoriteDatabaseAllUseCase,
    private val favoriteDatabaseDeleteUseCase: ProductsFavoriteDatabaseDeleteUseCase,
    private val databaseInsertUseCase: ProductsDatabaseInsertUseCase
) : ViewModel(), DefaultLifecycleObserver {
    private val events = MutableSharedFlow<Event>()
    val uiEvents: SharedFlow<Event> = events
    val data: ObservableField<List<RecyclerViewItem>> = ObservableField(emptyList())
    private var productItemsList: List<ProductItem> = mutableListOf()
    private var productCategoryListAll: List<ProductItem> = immutableListOf()
    private var productCategoryListFiltered: MutableSet<ProductItem> = mutableSetOf()
    var chipCategories: MutableMap<String, Boolean> = mutableMapOf()
    var isList = true
    var sortText: Int = R.string.price_lowest_to_highest
    var sortTypes: ObservableField<List<RecyclerViewItem>> = ObservableField(emptyList())

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        viewModelScope.launch {
            fetchProducts()
        }.invokeOnCompletion {
            createRecyclerItems()
        }
    }

    fun onSortClick() {
        viewModelScope.launch {
            events.emit(Event.SortClickEvent)
        }
        createRecyclerSort()
    }

    fun sortClosed() {
        createRecyclerItems()
    }

    private fun sortProducts(products: List<ProductItem>): List<ProductItem> {
        return when (sortText) {
            R.string.price_lowest_to_highest -> products.sortedBy {
                it.price.toFloat()
            }
            R.string.price_highest_to_lowest -> products.sortedByDescending {
                it.price.toFloat()
            }
            R.string.rating_lowest_to_highest -> products.sortedBy {
                it.rating
            }
            R.string.rating_highest_to_lowest -> products.sortedByDescending {
                it.rating
            }
            else -> {
                products
            }
        }
    }

    fun deleteFromDb(item: ProductItem) {
        viewModelScope.launch {
            when (favoriteDatabaseDeleteUseCase.execute(item.toProduct())) {
                is Resource.Success -> {
                    fetchProducts()
                }
                is Resource.Error -> events.emit(Event.DeleteErrorEvent)
                else -> {}
            }
        }.invokeOnCompletion {
            createRecyclerItems()
        }
    }

    fun addToCart(item: ProductItem) {
        viewModelScope.launch {
            when (databaseInsertUseCase.execute(item.toProduct())) {
                is Resource.Success -> events.emit(Event.AddToCartSuccess)
                is Resource.Error -> events.emit(Event.AddToCartError)
                else -> {}
            }
        }
    }

    fun onLayoutClick() {
        viewModelScope.launch {
            isList = !isList
            events.emit(Event.LayoutClickEvent)
            createRecyclerItems()
        }
    }

    private fun createRecyclerItems() {
        val newList = mutableListOf<RecyclerViewItem>()

        sortProducts(productItemsList).forEach { product ->
            newList.add(
                ProductVM(
                    product
                ).let {
                    viewModelScope.launch {
                        it.events().collectLatest { event ->
                            this@FavoritesViewModel.events.emit(event)
                        }
                    }
                    if (isList) it.toRecyclerViewItemList()
                    else it.toRecyclerViewItemGrid()
                }
            )
        }
        data.set(newList)
    }

    private fun createRecyclerSort() {
        val newList = mutableListOf<RecyclerViewItem>()
        val sorts = listOf(
            SortItem(R.string.price_lowest_to_highest, SortColorPreferences()),
            SortItem(R.string.price_highest_to_lowest, SortColorPreferences()),
            SortItem(R.string.rating_lowest_to_highest, SortColorPreferences()),
            SortItem(R.string.rating_highest_to_lowest, SortColorPreferences())
        )

        sorts.forEach { sort ->
            if (sort.text == sortText) {
                sort.sortColor = SortColorPreferences(
                    R.color.white,
                    R.color.red
                )
            } else {
                sort.sortColor = SortColorPreferences()
            }

            newList.add(
                SortVM(
                    sort
                ).let {
                    viewModelScope.launch {
                        it.events().collectLatest { event ->
                            this@FavoritesViewModel.events.emit(event)
                        }
                    }
                    it.toRecyclerViewItem()
                }
            )
        }
        sortTypes.set(newList)
    }

    private suspend fun fetchProducts() {
        when (val result = favoriteDatabaseAllUseCase.execute()) {
            is Resource.Success -> {
                result.data?.apply {
                    productItemsList = this.map {
                        ProductItem(
                            title = it.title,
                            brand = it.brand,
                            isNew = true,
                            price = it.price,
                            image = it.image,
                            colorText = it.colorText,
                            size = it.size,
                            category = it.category,
                            rating = it.rating.rate,
                            reviewCount = it.rating.count,
                            chosenColor = it.chosenColor,
                            chosenSize = it.chosenSize,
                            isFavorite = it.favorite
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
        object LayoutClickEvent : Event()
        object DeleteErrorEvent : Event()
        object AddToCartSuccess : Event()
        object AddToCartError : Event()
        object SortClickEvent : Event()
        class SortTypeClickEvent(val item: SortItem, val position: Int) : Event()
        class DeleteEvent(val item: ProductItem, val position: Int) : Event()
        class AddToCartEvent(val item: ProductItem, val position: Int) : Event()
        class ProductClickEvent(val item: ProductItem, val position: Int) : Event()
    }
}
