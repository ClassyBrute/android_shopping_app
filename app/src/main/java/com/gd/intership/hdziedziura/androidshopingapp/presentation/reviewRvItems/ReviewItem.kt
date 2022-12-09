package com.gd.intership.hdziedziura.androidshopingapp.presentation.reviewRvItems

import androidx.databinding.ObservableField
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem

data class ReviewItem(
    val name: String = "",
    val picture: Int = 0,
    val rating: Int = 0,
    val date: String = "",
    val text: String = "",
    val images: ObservableField<List<RecyclerViewItem>> = ObservableField(emptyList()),
    val hasPhotos: Boolean = false
)
