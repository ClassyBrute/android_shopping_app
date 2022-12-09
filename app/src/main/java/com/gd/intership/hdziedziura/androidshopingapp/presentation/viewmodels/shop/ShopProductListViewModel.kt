package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.databinding.ObservableField
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.data.mocks.mockSize
import com.gd.intership.hdziedziura.androidshopingapp.data.toProduct
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductCategoryUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductSubcategoryUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsFavoriteDatabaseAllUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsFavoriteDatabaseDeleteUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsFavoriteDatabaseInsertUseCase
import com.gd.intership.hdziedziura.androidshopingapp.presentation.shopRvItems.ColorItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.shopRvItems.ProductItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.shopRvItems.SizeItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.SortViewModel
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.item_vm.ColorVM
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.item_vm.ProductVM
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.item_vm.SizeVM
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.item_vm.toRecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.item_vm.toRecyclerViewItemFav
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.item_vm.toRecyclerViewItemGrid
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.item_vm.toRecyclerViewItemList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.internal.immutableListOf
import javax.inject.Inject

class ShopProductListViewModel @Inject constructor(
    private val productCategoryUseCase: ProductCategoryUseCase,
    private val productSubcategoryUseCase: ProductSubcategoryUseCase,
    private val favoriteDatabaseInsertUseCase: ProductsFavoriteDatabaseInsertUseCase,
    private val favoriteDatabaseAllUseCase: ProductsFavoriteDatabaseAllUseCase,
    private val favoriteDatabaseDeleteUseCase: ProductsFavoriteDatabaseDeleteUseCase,
    arguments: Bundle?
) : ViewModel(), DefaultLifecycleObserver {
    private val events = MutableSharedFlow<Event>()
    val uiEvents: SharedFlow<Event> = events
    val data: ObservableField<List<RecyclerViewItem>> = ObservableField(emptyList())
    private var productCategoryList: List<ProductItem> = mutableListOf()
    private var productCategoryListAll: List<ProductItem> = immutableListOf()
    private var productCategoryListFiltered: MutableSet<ProductItem> = mutableSetOf()
    var chipCategories: MutableMap<String, Boolean> = mutableMapOf()
    private var category: String = arguments?.getString("itemTitle") ?: ""
    var isList: Boolean = true
    var sortText: Int = R.string.price_lowest_to_highest

    val colors: ObservableField<List<RecyclerViewItem>> = ObservableField(emptyList())
    val sizes: ObservableField<List<RecyclerViewItem>> = ObservableField(emptyList())

    var sliderRange: ObservableField<List<Float>> = ObservableField(emptyList())
    var lowestPriceString = ObservableField("")
    var highestPriceString = ObservableField("")
    var valuesFromTo: ObservableField<List<Float>> = ObservableField(emptyList())

    private val allColors = mutableSetOf<String>()
    private val allSizes = mutableSetOf<String>()
    private var chosenColors = mutableListOf<Int>()
    private var chosenSizes = mutableListOf<String>()
    internal val sortViewModel by lazy {
        SortViewModel(viewModelScope)
    }

    val colorsFav: ObservableField<List<RecyclerViewItem>> = ObservableField(emptyList())
    var chosenColorFav: ObservableField<String> = ObservableField("Color")
    private var prevColorFav: ObservableField<ColorItem> = ObservableField()
    val sizesFav: ObservableField<List<RecyclerViewItem>> = ObservableField(emptyList())
    var chosenSizeFav: ObservableField<String> = ObservableField("Size")
    private var prevSizeFav: ObservableField<SizeItem> = ObservableField()

    private var product: ProductItem = ProductItem()

    init {
        viewModelScope.launch {
            fetchSubcategories()
            fetchProducts()
        }.invokeOnCompletion {
            createRecyclerItems()
            createColors()
            createSizes()
            initPriceRangeFilterData()
        }

        viewModelScope.launch {
            sortViewModel.sortEvents.collectLatest {
                when (it) {
                    is SortViewModel.SortEvent.OnSortSelected -> {
                        onSortClosed(it.titleResId)
                    }
                }
            }
        }
    }

    private fun initPriceRangeFilterData() {
        if (productCategoryListAll.isNotEmpty()) {
            val price = productCategoryListAll.sortedBy { it.price.toFloat() }
            sliderRange.set(listOf(price.first().price.toFloat(), price.last().price.toFloat()))
            valuesFromTo.set(listOf(price.first().price.toFloat(), price.last().price.toFloat()))
        }
    }

    fun onFiltersClick() {
        viewModelScope.launch {
            events.emit(Event.FiltersClickEvent)
        }
    }

    fun applyFilters() {
        val productCategorySequence = productCategoryListAll.asSequence()
            .filter { item ->
                val itemColors = mutableListOf<Int>()
                item.color.forEach { itemColors.add(Color.parseColor(it)) }
                chosenColors.any { it in itemColors } || chosenColors.isEmpty()
            }
            .filter { item ->
                val itemSizes = mutableListOf<String>()
                item.size.forEach { itemSizes.add(it) }
                chosenSizes.any { it in itemSizes } || chosenSizes.isEmpty()
            }
            .filterNot {
                it.price.toFloat() < sliderRange.get()?.get(0)!! ||
                    it.price.toFloat() > sliderRange.get()?.get(1)!!
            }

        productCategoryList = productCategorySequence.toList().minus(productCategoryListFiltered)
        createRecyclerItems()
    }

    fun onLayoutClick() {
        viewModelScope.launch {
            isList = !isList
            events.emit(Event.LayoutClickEvent)
            createRecyclerItems()
        }
    }

    fun onSortClick() {
        viewModelScope.launch {
            events.emit(Event.SortClickEvent)
        }
    }

    private suspend fun onSortClosed(@StringRes titleResId: Int) {
        sortText = titleResId
        events.emit(Event.SortClosedEvent(titleResId))
        createRecyclerItems()
    }

    fun checkChip(text: String) {
        val newList: MutableList<ProductItem> = productCategoryList.toMutableList()
        newList.addAll(
            productCategoryListFiltered.filter {
                it.title.contains(text)
            }
        )
        chipCategories[text] = true

        productCategoryListFiltered.removeAll(newList.toSet())
        productCategoryList = newList.distinctBy { it.title }
        createRecyclerItems()
    }

    fun uncheckChip(text: String) {
        productCategoryListFiltered.addAll(
            productCategoryListAll.filter {
                it.title.contains(text)
            }
        )
        chipCategories[text] = false

        productCategoryList = productCategoryList.filterNot {
            it.title.contains(text)
        }
        createRecyclerItems()
    }

    private fun createRecyclerItems() {
        val newList = mutableListOf<RecyclerViewItem>()

        sortViewModel.sortedProducts(productCategoryList).forEach { product ->
            newList.add(
                ProductVM(
                    product
                ).let {
                    viewModelScope.launch {
                        it.events().collectLatest { event ->
                            events.emit(event)
                        }
                    }
                    if (isList) it.toRecyclerViewItemList()
                    else it.toRecyclerViewItemGrid()
                }
            )
        }

        data.set(newList)
    }

    fun favoriteClick(item: ProductItem) {
        product = item

        if (item.isFavorite) {
            viewModelScope.launch {
                favoriteDatabaseDeleteUseCase.execute(item.toProduct())
                item.isFavorite = false
            }
        }

        createColorsFav(item)
        createSizesFav(item)
    }

    private fun createColorsFav(product: ProductItem) {
        val list = mutableListOf<RecyclerViewItem>()

        product.color.forEachIndexed { index, color ->
            list.add(
                ColorVM(
                    ColorItem(
                        Color.parseColor(color),
                        product.colorText[index]
                    )
                ).let {
                    viewModelScope.launch {
                        it.events().collectLatest { event ->
                            if (event is Event.ColorClickEvent) {
                                if (event.item == prevColorFav.get()) {
                                    event.item.checked.set(!event.item.checked.get())
                                    chosenColorFav.set("Color")
                                    prevColorFav.set(ColorItem())
                                } else {
                                    chosenColorFav.set(event.item.colorText)
                                    event.item.checked.set(true)
                                    prevColorFav.get()?.checked?.set(false)
                                    prevColorFav.set(event.item)

                                    if (chosenColorFav.get() != "Color" && chosenSizeFav.get() != "Size") {
                                        viewModelScope.launch {
                                            product.isFavorite = true
                                            product.chosenColor = chosenColorFav.get().toString()
                                            product.chosenSize = chosenSizeFav.get().toString()
                                            favoriteDatabaseInsertUseCase.execute(product.toProduct())
                                            events.emit(Event.ColorsSheetDismiss)
                                            events.emit(Event.AddedToFavorites)
                                        }
                                    }

                                    events.emit(Event.ColorsSheetDismiss)
                                }
                            }
                        }
                    }
                    it.toRecyclerViewItem()
                }
            )
        }

        colorsFav.set(list)
    }

    private fun createSizesFav(product: ProductItem) {
        val list = mutableListOf<RecyclerViewItem>()

        product.size
            .sortedWith(compareBy(mockSize::indexOf))
            .forEach { size ->
                list.add(
                    SizeVM(
                        SizeItem(
                            size
                        )
                    ).let {
                        viewModelScope.launch {
                            it.events().collectLatest { event ->
                                if (event is Event.SizeClickEvent) {
                                    if (event.item == prevSizeFav.get()) {
                                        event.item.checked.set(!event.item.checked.get())
                                        chosenSizeFav.set("Size")
                                        prevSizeFav.set(SizeItem())
                                    } else {
                                        chosenSizeFav.set(event.item.size)
                                        event.item.checked.set(true)
                                        prevSizeFav.get()?.checked?.set(false)
                                        prevSizeFav.set(event.item)
                                        events.emit(Event.SizesSheetDismiss)
                                    }
                                }
                            }
                        }
                        it.toRecyclerViewItemFav()
                    }
                )
            }

        sizesFav.set(list)
    }

    private suspend fun fetchProducts() {
        when (val result = productCategoryUseCase.execute(category.lowercase())) {
            is Resource.Success -> {
                val favorites = favoriteDatabaseAllUseCase.execute()
                result.data?.apply {
                    productCategoryList = this.map {
                        ProductItem(
                            title = it.title,
                            brand = it.brand,
                            price = it.price,
                            category = it.category,
                            rating = it.rating.rate,
                            reviewCount = it.rating.count,
                            image = it.image,
                            isNew = true,
                            color = it.colorHex,
                            colorText = it.colorText,
                            size = it.size,
                            isFavorite = favorites.data?.any { product ->
                                it.title == product.title
                            } == true,
                            chosenColor = favorites.data?.find { product ->
                                it.title == product.title
                            }?.chosenColor.toString(),
                            chosenSize = favorites.data?.find { product ->
                                it.title == product.title
                            }?.chosenSize.toString()
                        )
                    }
                }
                productCategoryListAll = productCategoryList
            }
            is Resource.Error -> {
                events.emit(Event.FetchingErrorEvent)
            }
            else -> {
            }
        }
    }

    private suspend fun fetchSubcategories() {
        when (val result = productSubcategoryUseCase.execute(category.lowercase())) {
            is Resource.Success -> {
                chipCategories = result.data?.toMutableMap()!!
            }
            is Resource.Error -> {
                events.emit(Event.FetchingErrorEvent)
            }
            else -> {
            }
        }
    }

    private fun createColors() {
        if (productCategoryListAll.isNotEmpty()) {
            val list = mutableListOf<RecyclerViewItem>()

            productCategoryListAll.forEach {
                allColors.addAll(it.color)
            }

            allColors.forEach { color ->
                list.add(
                    ColorVM(
                        ColorItem(Color.parseColor(color))
                    ).let {
                        viewModelScope.launch {
                            it.events().collectLatest { event ->
                                if (event is Event.ColorClickEvent) {
                                    if (chosenColors.contains(event.item.color)) {
                                        chosenColors.remove(event.item.color)
                                    } else {
                                        chosenColors.add(event.item.color)
                                    }
                                }
                            }
                        }
                        it.toRecyclerViewItem()
                    }
                )
            }

            colors.set(list)
        }
    }

    private fun createSizes() {
        if (productCategoryListAll.isNotEmpty()) {
            val list = mutableListOf<RecyclerViewItem>()

            productCategoryListAll.forEach {
                allSizes.addAll(it.size)
            }

            allSizes
                .sortedWith(compareBy(mockSize::indexOf))
                .forEach { size ->
                    list.add(
                        SizeVM(
                            SizeItem(size)
                        ).let {
                            viewModelScope.launch {
                                it.events().collectLatest { event ->
                                    if (event is Event.SizeClickEvent) {
                                        if (chosenSizes.contains(event.item.size)) {
                                            chosenSizes.remove(event.item.size)
                                        } else {
                                            chosenSizes.add(event.item.size)
                                        }
                                    }
                                }
                            }
                            it.toRecyclerViewItem()
                        }
                    )
                }

            sizes.set(list)
        }
    }

    sealed class Event {
        object FetchingErrorEvent : Event()
        object LayoutClickEvent : Event()
        object SortClickEvent : Event()
        object FiltersClickEvent : Event()
        class SortClosedEvent(@StringRes val titleResId: Int) : Event()
        class ColorClickEvent(val item: ColorItem, val position: Int) : Event()
        class SizeClickEvent(val item: SizeItem, val position: Int) : Event()
        class ProductClickEvent(val item: ProductItem, val position: Int) : Event()
        class FavoriteClickEvent(val item: ProductItem, val position: Int, val view: View) : Event()
        object AddedToFavorites : Event()
        object ColorsSheetDismiss : Event()
        object SizesSheetDismiss : Event()
    }
}
