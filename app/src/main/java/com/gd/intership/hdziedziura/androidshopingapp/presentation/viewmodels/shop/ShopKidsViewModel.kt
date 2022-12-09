package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop

import androidx.databinding.ObservableField
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.CategoriesAllUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductsAllUseCase
import com.gd.intership.hdziedziura.androidshopingapp.presentation.homeRvItems.ProductItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.shopRvItems.BannerItemShop
import com.gd.intership.hdziedziura.androidshopingapp.presentation.shopRvItems.CategoryItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.item_vm.BannerShopVM
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.item_vm.CategoryVM
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.item_vm.toRecyclerView
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.item_vm.toRecyclerViewItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopKidsViewModel @Inject constructor(
    private val categoryUseCase: CategoriesAllUseCase,
    private val productsUseCase: ProductsAllUseCase,
) : ViewModel(), DefaultLifecycleObserver {
    val events = MutableSharedFlow<ShopViewModel.Event>()
    val uiEvents: SharedFlow<ShopViewModel.Event> = events
    val data: ObservableField<List<RecyclerViewItem>> = ObservableField(emptyList())
    private var productItemsList: List<ProductItem> = mutableListOf()
    private var categoriesList: List<String> = mutableListOf()

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        viewModelScope.launch {
            fetchCategories()
            fetchProducts()
        }.invokeOnCompletion {
            createRecyclerItems()
        }
    }

    private fun createRecyclerItems() {
        val newList = mutableListOf<RecyclerViewItem>()

        newList.add(
            BannerShopVM(
                BannerItemShop(
                    R.string.kids_sale,
                    R.string.sale_amount,
                    R.string.kids
                )
            )
                .let {
                    viewModelScope.launch {
                        it.events().debounce { 300L }
                            .collectLatest { this@ShopKidsViewModel.events.emit(it) }
                    }
                    it.toRecyclerView()
                }
        )

        categoriesList.forEach { category ->
            val product = productItemsList.filter { it.category == category }
            newList.add(
                CategoryVM(
                    CategoryItem(
                        category.replaceFirstChar { it.uppercase() },
                        product[0].image,
                        R.string.kids
                    )
                ).let {
                    viewModelScope.launch {
                        it.events().debounce { 300L }
                            .collectLatest { this@ShopKidsViewModel.events.emit(it) }
                    }
                    it.toRecyclerViewItem()
                }
            )
        }
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
                events.emit(ShopViewModel.Event.FetchingErrorEvent)
            }
            else -> {}
        }
    }

    private suspend fun fetchProducts() {
        when (val result = productsUseCase.execute()) {
            is Resource.Success -> {
                result.data?.apply {
                    productItemsList = this.map {
                        ProductItem(
                            category = it.category,
                            image = it.image
                        )
                    }
                }
            }

            is Resource.Error -> {
                events.emit(ShopViewModel.Event.FetchingErrorEvent)
            }
            else -> {}
        }
    }
}
