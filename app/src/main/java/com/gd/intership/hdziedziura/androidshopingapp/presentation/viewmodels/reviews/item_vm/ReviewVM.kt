package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.reviews.item_vm

import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.common.ItemWithEvent
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.reviewRvItems.ReviewItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.item_vm.ItemVm
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.reviews.ProductReviewsViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

data class ReviewVM(override var item: ReviewItem) :
    ItemVm<ReviewItem>(),
    ItemWithEvent<ProductReviewsViewModel.Event> {
    private val events: MutableSharedFlow<ProductReviewsViewModel.Event> = MutableSharedFlow()

    fun onReviewClick() {
        itemsScope.launch {
            events.emit(ProductReviewsViewModel.Event.ReviewClickEvent(item, position))
        }
    }

    override fun events(): SharedFlow<ProductReviewsViewModel.Event> {
        return events
    }
}

fun ReviewVM.toRecyclerViewItem() = RecyclerViewItem(R.layout.item_review, this)
