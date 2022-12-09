package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.reviews.item_vm

import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.common.ItemWithEvent
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.reviewRvItems.ReviewImageUriItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.item_vm.ItemVm
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.reviews.ProductReviewsViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

data class ReviewImageUriVM(
    override val item: ReviewImageUriItem
) : ItemVm<ReviewImageUriItem>(), ItemWithEvent<ProductReviewsViewModel.Event> {
    private val events: MutableSharedFlow<ProductReviewsViewModel.Event> = MutableSharedFlow()

    fun onRemove() {
        itemsScope.launch {
            events.emit(ProductReviewsViewModel.Event.RemovePhotoEvent(item, position))
        }
    }

    override fun events(): SharedFlow<ProductReviewsViewModel.Event> {
        return events
    }
}

fun ReviewImageUriVM.toRecyclerViewItem() = RecyclerViewItem(R.layout.item_review_uri_image, this)
