package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.reviews.item_vm

import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.common.ItemWithEvent
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.reviewRvItems.ReviewImageItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.item_vm.ItemVm
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.reviews.ProductReviewsViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

data class ReviewImageVM(
    override val item: ReviewImageItem
) : ItemVm<ReviewImageItem>(), ItemWithEvent<ProductReviewsViewModel.Event> {
    private val events: MutableSharedFlow<ProductReviewsViewModel.Event> = MutableSharedFlow()

    override fun events(): SharedFlow<ProductReviewsViewModel.Event> {
        return events
    }
}

fun ReviewImageVM.toRecyclerViewItem() = RecyclerViewItem(R.layout.item_review_image, this)
