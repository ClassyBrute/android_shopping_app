package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.details

import android.graphics.Color
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.data.mocks.mockSize
import com.gd.intership.hdziedziura.androidshopingapp.data.toProduct
import com.gd.intership.hdziedziura.androidshopingapp.domain.model.Product
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductCategoryUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsDatabaseInsertUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsFavoriteDatabaseAllUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsFavoriteDatabaseDeleteUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsFavoriteDatabaseInsertUseCase
import com.gd.intership.hdziedziura.androidshopingapp.presentation.homeRvItems.ProductItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.shopRvItems.ColorItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.shopRvItems.SizeItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.details.item_vm.ColorVM
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.details.item_vm.ProductVM
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.details.item_vm.SizeVM
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.details.item_vm.toRecyclerViewItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductDetailsViewModel @Inject constructor(
    private val productCategoryUseCase: ProductCategoryUseCase,
    private val databaseInsertUseCase: ProductsDatabaseInsertUseCase,
    private val favoriteDatabaseInsertUseCase: ProductsFavoriteDatabaseInsertUseCase,
    private val favoriteDatabaseAllUseCase: ProductsFavoriteDatabaseAllUseCase,
    private val favoriteDatabaseDeleteUseCase: ProductsFavoriteDatabaseDeleteUseCase
) : ViewModel(), DefaultLifecycleObserver {
    private val events = MutableSharedFlow<Event>()
    val uiEvents: SharedFlow<Event> = events
    val data: ObservableField<List<RecyclerViewItem>> = ObservableField(emptyList())
    private var productCategoryList: List<ProductItem> = mutableListOf()
    var product: ObservableField<ProductItem> = ObservableField()
    private var productSmall: ProductItem = ProductItem()
    var category: String = ""
    var title: String = ""

    val colors: ObservableField<List<RecyclerViewItem>> = ObservableField(emptyList())
    val sizes: ObservableField<List<RecyclerViewItem>> = ObservableField(emptyList())
    val colorsFav: ObservableField<List<RecyclerViewItem>> = ObservableField(emptyList())
    val sizesFav: ObservableField<List<RecyclerViewItem>> = ObservableField(emptyList())
    var recyclerElements: ObservableField<String> = ObservableField("")

    var chosenSize: ObservableField<String> = ObservableField("Size")
    private var prevSize: ObservableField<SizeItem> = ObservableField()
    var chosenColor: ObservableField<String> = ObservableField("Color")
    private var prevColor: ObservableField<ColorItem> = ObservableField()

    var chosenSizeSmall: ObservableField<String> = ObservableField("Size")
    private var prevSizeSmall: ObservableField<SizeItem> = ObservableField()
    var chosenColorSmall: ObservableField<String> = ObservableField("Color")
    private var prevColorSmall: ObservableField<ColorItem> = ObservableField()

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        viewModelScope.launch {
            fetchProducts()
        }.invokeOnCompletion {
            createRecyclerItems()
            createSizes()
            createColors()
        }
    }

    fun favoriteClick(item: ProductItem) {
        productSmall = item

        if (item.isFavorite) {
            viewModelScope.launch {
                favoriteDatabaseDeleteUseCase.execute(item.toProduct())
                item.isFavorite = false
            }
        }

        createColorsSmall(item)
        createSizesSmall(item)
    }

    fun favoriteBigClick(view: View) {
        viewModelScope.launch {
            events.emit(Event.FavoriteClickEvent(product.get()!!, 0, view))
        }
    }

    fun onAddToCartClick() {
        viewModelScope.launch {
            events.emit(Event.AddToCartEvent)
        }
    }

    fun addToCart() {
        viewModelScope.launch {
            val productDB = product.get().let {
                Product(
                    title = it?.title!!,
                    price = it.price,
                    image = it.image,
                    colorText = listOf(chosenColor.get()!!),
                    size = listOf(chosenSize.get()!!),
                    category = it.category,
                    rating = Product.Rate(it.rating, it.reviewCount)
                )
            }

            when (databaseInsertUseCase.execute(productDB)) {
                is Resource.Success -> events.emit(Event.CartSuccessEvent)
                is Resource.Error -> events.emit(Event.CartFailEvent)
                else -> {}
            }
        }
    }

    fun onSizeClick() {
        viewModelScope.launch {
            events.emit(Event.SizesClickEvent)
        }
    }

    fun onColorClick() {
        viewModelScope.launch {
            events.emit(Event.ColorsClickEvent)
        }
    }

    fun onReviewsClick() {
        viewModelScope.launch {
            product.get()?.let { Event.ReviewsClickEvent(it) }?.let { events.emit(it) }
        }
    }

    private fun createRecyclerItems() {
        val newList = mutableListOf<RecyclerViewItem>()

        productCategoryList
            .filterNot { it.title == title }
            .forEach { product ->
                newList.add(
                    ProductVM(
                        product
                    ).let {
                        viewModelScope.launch {
                            it.events().collectLatest { event ->
                                events.emit(event)
                            }
                        }
                        it.toRecyclerViewItem()
                    }
                )
            }

        data.set(newList)
    }

    private fun createSizes() {
        val list = mutableListOf<RecyclerViewItem>()

        product.get()?.size
            ?.sortedWith(compareBy(mockSize::indexOf))
            ?.forEach { size ->
                list.add(
                    SizeVM(
                        SizeItem(size)
                    ).let {
                        viewModelScope.launch {
                            it.events().collectLatest { event ->
                                if (event is Event.SizeClickEvent) {
                                    if (event.item == prevSize.get()) {
                                        event.item.checked.set(!event.item.checked.get())
                                        chosenSize.set("Size")
                                        prevSize.set(SizeItem())
                                    } else {
                                        chosenSize.set(event.item.size)
                                        event.item.checked.set(true)
                                        prevSize.get()?.checked?.set(false)
                                        prevSize.set(event.item)
                                        events.emit(Event.SizesSheetDismiss)
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

    private fun createSizesSmall(product: ProductItem) {
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
                                    if (event.item == prevSizeSmall.get()) {
                                        event.item.checked.set(!event.item.checked.get())
                                        chosenSizeSmall.set("Size")
                                        prevSizeSmall.set(SizeItem())
                                    } else {
                                        chosenSizeSmall.set(event.item.size)
                                        event.item.checked.set(true)
                                        prevSizeSmall.get()?.checked?.set(false)
                                        prevSizeSmall.set(event.item)
                                        events.emit(Event.SizesSheetFavDismiss)
                                    }
                                }
                            }
                        }
                        it.toRecyclerViewItem()
                    }
                )
            }

        sizesFav.set(list)
    }

    private fun createColors() {
        val list = mutableListOf<RecyclerViewItem>()

        product.get()?.color
            ?.forEachIndexed { index, color ->
                list.add(
                    ColorVM(
                        ColorItem(
                            Color.parseColor(color),
                            product.get()?.colorText?.get(index) ?: ""
                        )
                    ).let {
                        viewModelScope.launch {
                            it.events().collectLatest { event ->
                                if (event is Event.ColorClickEvent) {
                                    if (event.item == prevColor.get()) {
                                        event.item.checked.set(!event.item.checked.get())
                                        chosenColor.set("Color")
                                        prevColor.set(ColorItem())
                                    } else {
                                        chosenColor.set(event.item.colorText)
                                        event.item.checked.set(true)
                                        prevColor.get()?.checked?.set(false)
                                        prevColor.set(event.item)
                                        events.emit(Event.ColorsSheetDismiss)
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

    private fun createColorsSmall(product: ProductItem) {
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
                                if (event.item == prevColorSmall.get()) {
                                    event.item.checked.set(!event.item.checked.get())
                                    chosenColorSmall.set("Color")
                                    prevColorSmall.set(ColorItem())
                                } else {
                                    chosenColorSmall.set(event.item.colorText)
                                    event.item.checked.set(true)
                                    prevColorSmall.get()?.checked?.set(false)
                                    prevColorSmall.set(event.item)

                                    if (chosenColorSmall.get() != "Color" && chosenSizeSmall.get() != "Size") {
                                        viewModelScope.launch {
                                            product.isFavorite = true
                                            product.chosenColor = chosenColorSmall.get().toString()
                                            product.chosenSize = chosenSizeSmall.get().toString()
                                            favoriteDatabaseInsertUseCase.execute(product.toProduct())
                                            events.emit(Event.ColorsSheetFavDismiss)
                                            events.emit(Event.AddedToFavorites)
                                        }
                                    }
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
                            description = it.description,
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
                product.set(productCategoryList.find { it.title == title })
                recyclerElements.set("${productCategoryList.size} items")
            }
            is Resource.Error -> {
                events.emit(Event.FetchingErrorEvent)
            }
            else -> {
            }
        }
    }

    sealed class Event {
        object FetchingErrorEvent : Event()
        object SizesClickEvent : Event()
        object ColorsClickEvent : Event()
        object CartSuccessEvent : Event()
        object CartFailEvent : Event()
        object AddToCartEvent : Event()
        object SizesSheetDismiss : Event()
        object ColorsSheetDismiss : Event()
        object SizesSheetFavDismiss : Event()
        object ColorsSheetFavDismiss : Event()
        class ReviewsClickEvent(val item: ProductItem) : Event()
        class SizeClickEvent(val item: SizeItem, val position: Int) : Event()
        class ColorClickEvent(val item: ColorItem, val position: Int) : Event()
        class ProductClickEvent(val item: ProductItem, val position: Int) : Event()
        class FavoriteClickEvent(val item: ProductItem, val position: Int, val view: View) : Event()
        object AddedToFavorites : Event()
    }
}
