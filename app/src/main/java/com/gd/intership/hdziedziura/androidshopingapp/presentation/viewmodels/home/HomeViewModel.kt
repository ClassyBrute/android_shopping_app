package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home

import android.graphics.Color
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.data.mocks.mockSize
import com.gd.intership.hdziedziura.androidshopingapp.data.toProduct
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.CategoriesAllUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsAllUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsFavoriteDatabaseAllUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsFavoriteDatabaseDeleteUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsFavoriteDatabaseInsertUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user.UserStatus
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user.UserStream
import com.gd.intership.hdziedziura.androidshopingapp.presentation.homeRvItems.BannerItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.homeRvItems.BannerItemHome
import com.gd.intership.hdziedziura.androidshopingapp.presentation.homeRvItems.CarouselItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.homeRvItems.ProductItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.shopRvItems.ColorItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.shopRvItems.SizeItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.details.ProductDetailsViewModel
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.details.item_vm.ColorVM
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.details.item_vm.SizeVM
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.details.item_vm.toRecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.item_vm.BannerBigVM
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.item_vm.BannerVM
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.item_vm.CarouselVM
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.item_vm.toRecyclerView
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.item_vm.toRecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.item_vm.toRecyclerViewItemBig
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.List
import kotlin.collections.emptyList
import kotlin.collections.filter
import kotlin.collections.forEach
import kotlin.collections.map
import kotlin.collections.mutableListOf

class HomeViewModel @Inject constructor(
    private val productsAllUseCase: ProductsAllUseCase,
    private val categoryUseCase: CategoriesAllUseCase,
    private val userStream: UserStream,
    private val favoriteDatabaseInsertUseCase: ProductsFavoriteDatabaseInsertUseCase,
    private val favoriteDatabaseAllUseCase: ProductsFavoriteDatabaseAllUseCase,
    private val favoriteDatabaseDeleteUseCase: ProductsFavoriteDatabaseDeleteUseCase
) : ViewModel(), DefaultLifecycleObserver {
    val events = MutableSharedFlow<Event>()
    val uiEvents: SharedFlow<Event> = events
    var data: ObservableField<List<RecyclerViewItem>> = ObservableField(emptyList())
    private var productItemsList: List<ProductItem> = mutableListOf()
    private var categoriesList: List<String> = mutableListOf()
    private var homeJob: Job? = null

    val colors: ObservableField<List<RecyclerViewItem>> = ObservableField(emptyList())
    var chosenColor: ObservableField<String> = ObservableField("Color")
    private var prevColor: ObservableField<ColorItem> = ObservableField()
    val sizes: ObservableField<List<RecyclerViewItem>> = ObservableField(emptyList())
    var chosenSize: ObservableField<String> = ObservableField("Size")
    private var prevSize: ObservableField<SizeItem> = ObservableField()

    private var product: ProductItem = ProductItem()

    init {
        homeJob?.cancel()
        homeJob = viewModelScope.launch {
            userStream.userStatus.collectLatest {
                if (it == UserStatus.Authorized) {
                    launch {
                        fetchCategories()
                        fetchProducts()
                    }.invokeOnCompletion {
                        createRecyclerItems()
                    }
                }
            }
        }
    }

    // TODO icon in home doesn't change when deleted in favorites page
    override fun onResume(owner: LifecycleOwner) {
        listenAuthStatus()
    }

    private fun listenAuthStatus() {
        viewModelScope.launch {
            userStream.userStatus.collectLatest {
                if (it == UserStatus.NonAuthorized)
                    events.emit(Event.NavigateToAuth)
            }
        }
    }

    fun favoriteClick(item: ProductItem) {
        product = item

        if (item.isFavorite) {
            viewModelScope.launch {
                favoriteDatabaseDeleteUseCase.execute(item.toProduct())
                item.isFavorite = false
            }
        }

        createColors(item)
        createSizes(item)
    }

    private fun createColors(product: ProductItem) {
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
                            if (event is ProductDetailsViewModel.Event.ColorClickEvent) {
                                if (event.item == prevColor.get()) {
                                    event.item.checked.set(!event.item.checked.get())
                                    chosenColor.set("Color")
                                    prevColor.set(ColorItem())
                                } else {
                                    chosenColor.set(event.item.colorText)
                                    event.item.checked.set(true)
                                    prevColor.get()?.checked?.set(false)
                                    prevColor.set(event.item)

                                    if (chosenColor.get() != "Color" && chosenSize.get() != "Size") {
                                        viewModelScope.launch {
                                            product.isFavorite = true
                                            product.chosenColor = chosenColor.get().toString()
                                            product.chosenSize = chosenSize.get().toString()
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

        colors.set(list)
    }

    private fun createSizes(product: ProductItem) {
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
                                if (event is ProductDetailsViewModel.Event.SizeClickEvent) {
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

    private fun createRecyclerItems() {
        val newList = mutableListOf<RecyclerViewItem>()
        newList.add(
            BannerVM(
                BannerItem(
                    R.string.fashion_sale,
                    R.drawable.image_top,
                    "Women's clothing"
                )
            ).let {
                viewModelScope.launch {
                    it.events().debounce { 300L }
                        .collectLatest { this@HomeViewModel.events.emit(it) }
                }
                it.toRecyclerView()
            }
        )

        categoriesList.forEach { category ->
            newList.add(
                CarouselVM(
                    CarouselItem(
                        category.replaceFirstChar { it.uppercase() },
                        R.string.never_seen,
                        productItemsList.filter { it.category == category }
                    )
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

        newList.add(
            BannerBigVM(
                BannerItemHome(
                    R.string.new_collection,
                    R.string.men_hoodies,
                    R.string.black,
                    R.string.summer_sale,
                    R.drawable.image_top,
                    R.drawable.image_right,
                    R.drawable.image_bottom,
                    R.color.white,
                    "Women's clothing",
                    "Men's clothing",
                    "Women's clothing",
                    "Electronics"
                )
            ).let {
                viewModelScope.launch {
                    it.events().debounce { 300L }
                        .collectLatest { this@HomeViewModel.events.emit(it) }
                }
                it.toRecyclerViewItemBig()
            }
        )

        data.set(newList)
    }

    private suspend fun fetchCategories() {
        when (val result = categoryUseCase.execute()) {
            is Resource.Success -> {
                result.data?.apply {
                    categoriesList = this
                }
            }
            is Resource.Error -> {
                events.emit(Event.FetchingErrorEvent)
            }
            else -> {}
        }
    }

    private suspend fun fetchProducts() {
        when (val result = productsAllUseCase.execute()) {
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
        class BannerClickEvent(val item: BannerItem, val position: Int) : Event()
        class BannerBigTopClickEvent(val item: BannerItemHome, val position: Int) : Event()
        class BannerBigRightClickEvent(val item: BannerItemHome, val position: Int) : Event()
        class BannerBigLeftClickEvent(val item: BannerItemHome, val position: Int) : Event()
        class BannerBigBottomClickEvent(val item: BannerItemHome, val position: Int) : Event()
        class ProductClickEvent(val item: ProductItem, val position: Int) : Event()
        class FavoriteClickEvent(val item: ProductItem, val position: Int, val view: View) : Event()
        class CarouselClickEvent(val item: CarouselItem, val position: Int) : Event()
        object AddedToFavorites : Event()
        object ColorsSheetDismiss : Event()
        object SizesSheetDismiss : Event()
        object NavigateToAuth : Event()
    }
}
