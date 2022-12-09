package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.databinding.ObservableField
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.presentation.shopRvItems.ProductItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

private const val DEFAULT_TEXT_COLOR = R.color.black
private const val SELECTED_TEXT_COLOR = R.color.white
private const val DEFAULT_BACKGROUND_COLOR = R.color.white
private const val SELECTED_BACKGROUND_COLOR = R.color.red

internal class SortViewModel(private val viewModelScope: CoroutineScope) {

    private val events = MutableSharedFlow<SortEvent>()
    val sortEvents: SharedFlow<SortEvent> = events

    val colorsPriceAsc = ObservableField(SortColorPreferences(SELECTED_TEXT_COLOR, SELECTED_BACKGROUND_COLOR))
    val colorsPriceDesc = ObservableField(SortColorPreferences())
    val colorsRatingAsc = ObservableField(SortColorPreferences())
    val colorsRatingDesc = ObservableField(SortColorPreferences())

    private var order: Order = Order.PriceAsc

    fun onPriceLowToHighClick() {
        order = Order.PriceAsc
        sendSortEvent(R.string.price_lowest_to_highest)
        checkColors()
    }

    fun onPriceHighToLowClick() {
        order = Order.PriceDesc
        sendSortEvent(R.string.price_highest_to_lowest)
        checkColors()
    }

    fun onRatingLowToHighClick() {
        order = Order.RatingAsc
        sendSortEvent(R.string.rating_lowest_to_highest)
        checkColors()
    }

    fun onRatingHighToLowClick() {
        order = Order.RatingDesc
        sendSortEvent(R.string.rating_highest_to_lowest)
        checkColors()
    }

    private fun resetColorsToDefault() {
        colorsPriceAsc.set(SortColorPreferences())
        colorsPriceDesc.set(SortColorPreferences())
        colorsRatingAsc.set(SortColorPreferences())
        colorsRatingDesc.set(SortColorPreferences())
    }

    private fun checkColors() {
        resetColorsToDefault()
        when (order) {
            Order.PriceAsc -> colorsPriceAsc.set(
                SortColorPreferences(
                    SELECTED_TEXT_COLOR,
                    SELECTED_BACKGROUND_COLOR
                )
            )
            Order.PriceDesc -> colorsPriceDesc.set(
                SortColorPreferences(
                    SELECTED_TEXT_COLOR,
                    SELECTED_BACKGROUND_COLOR
                )
            )
            Order.RatingAsc -> colorsRatingAsc.set(
                SortColorPreferences(
                    SELECTED_TEXT_COLOR,
                    SELECTED_BACKGROUND_COLOR
                )
            )
            Order.RatingDesc -> colorsRatingDesc.set(
                SortColorPreferences(
                    SELECTED_TEXT_COLOR,
                    SELECTED_BACKGROUND_COLOR
                )
            )
        }
    }

    internal fun sortedProducts(productCategoryList: List<ProductItem>): List<ProductItem> {
        return when (order) {
            Order.PriceAsc -> {
                productCategoryList.sortedBy {
                    it.price.toFloat()
                }
            }
            Order.PriceDesc -> {
                productCategoryList.sortedByDescending {
                    it.price.toFloat()
                }
            }
            Order.RatingAsc -> {
                productCategoryList.sortedBy {
                    it.rating
                }
            }
            Order.RatingDesc -> {
                productCategoryList.sortedByDescending {
                    it.rating
                }
            }
        }
    }

    private fun sendSortEvent(@StringRes titleResId: Int) {
        viewModelScope.launch {
            events.emit(SortEvent.OnSortSelected(titleResId))
        }
    }

    enum class Order {
        PriceAsc,
        PriceDesc,
        RatingAsc,
        RatingDesc
    }

    sealed class SortEvent {
        data class OnSortSelected(@StringRes val titleResId: Int) : SortEvent()
    }
}

data class SortColorPreferences(
    @ColorRes val textColorId: Int = DEFAULT_TEXT_COLOR,
    @ColorRes val backgroundColorId: Int = DEFAULT_BACKGROUND_COLOR
)
