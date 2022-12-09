package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.reviews

import android.net.Uri
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.domain.model.Product
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.product.ProductCategoryUseCase
import com.gd.intership.hdziedziura.androidshopingapp.presentation.homeRvItems.ProductItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.reviewRvItems.ReviewImageItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.reviewRvItems.ReviewImageUriItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.reviewRvItems.ReviewItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.reviews.item_vm.ReviewImageUriVM
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.reviews.item_vm.ReviewImageVM
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.reviews.item_vm.ReviewVM
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.reviews.item_vm.toRecyclerViewItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductReviewsViewModel @Inject constructor(
    private val productCategoryUseCase: ProductCategoryUseCase
) : ViewModel(), DefaultLifecycleObserver {
    private val events = MutableSharedFlow<Event>()
    val uiEvents: SharedFlow<Event> = events
    val data: ObservableField<List<RecyclerViewItem>> = ObservableField(emptyList())
    private val reviewsWithPhotos: ObservableField<List<RecyclerViewItem>> = ObservableField(emptyList())
    private val reviewsAll: ObservableField<List<RecyclerViewItem>> = ObservableField(emptyList())
    private var productCategoryList: List<ProductItem> = mutableListOf()
    var product: ObservableField<ProductItem> = ObservableField()
    var category: String = ""
    var title: String = ""
    var reviewsCount = ObservableInt(1)
    var dataSize: ObservableInt = ObservableInt(0)
    var withPhoto = ObservableBoolean()
    var rating = ObservableField(0f)
    var review = ObservableField("")
    val addedPhotos: ObservableField<List<RecyclerViewItem>> = ObservableField(emptyList())
    var addedPhotosUriList: MutableList<Uri> = mutableListOf()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        viewModelScope.launch {
            fetchProducts()
        }.invokeOnCompletion {
            reviewsCount.set(
                product.get()?.five!! + product.get()?.four!! + product.get()?.three!! +
                    product.get()?.two!! + product.get()?.one!!
            )
            createRecyclerItems()
            dataSize.set(data.get()?.size!!)
        }
    }

    fun onCheckboxClick() {
        if (withPhoto.get()) data.set(reviewsWithPhotos.get())
        else data.set(reviewsAll.get())

        dataSize.set(data.get()?.size!!)
    }

    fun onWriteReviewClick() {
        viewModelScope.launch {
            events.emit(Event.WriteReviewEvent)
        }
    }

    fun onAddPhotoClick() {
        viewModelScope.launch {
            events.emit(Event.AddPhotoEvent)
        }
    }

    fun createPhotos() {
        val imageList = mutableListOf<RecyclerViewItem>()

        addedPhotosUriList.forEach { uri ->
            imageList.add(
                ReviewImageUriVM(
                    ReviewImageUriItem(
                        uri
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

        addedPhotos.set(imageList)
    }

    fun onSendReviewClick() {
        product.get()?.reviews?.add(
            Product.Review(
                "New Reviewer",
                R.drawable.image_right,
                rating.get()?.toInt()!!,
                "May 12, 2022",
                review.get().toString(),
                if (addedPhotosUriList.isEmpty()) listOf("") else addedPhotosUriList
            )
        )
        viewModelScope.launch {
            events.emit(Event.CloseBottomSheetEvent)
        }

        createRecyclerItems()

        if (withPhoto.get()) data.set(reviewsWithPhotos.get())
        else data.set(reviewsAll.get())

        dataSize.set(data.get()?.size!!)
    }

    private suspend fun fetchProducts() {
        when (val result = productCategoryUseCase.execute(category.lowercase())) {
            is Resource.Success -> {
                result.data?.apply {
                    productCategoryList = this.map {
                        ProductItem(
                            title = it.title,
                            rating = it.rating.rate,
                            reviewCount = it.rating.count,
                            five = it.rating.five,
                            four = it.rating.four,
                            three = it.rating.three,
                            two = it.rating.two,
                            one = it.rating.one,
                            reviews = it.reviews,
                        )
                    }
                }
                product.set(productCategoryList.find { it.title == title })
            }
            is Resource.Error -> {
                events.emit(Event.FetchingErrorEvent)
            }
            else -> {
            }
        }
    }

    private fun createRecyclerItems() {
        val newList = mutableListOf<RecyclerViewItem>()
        val newListWithPhotos = mutableListOf<RecyclerViewItem>()

        product.get()?.reviews?.forEach { review ->
            val image: ObservableField<List<RecyclerViewItem>> = ObservableField(emptyList())

            newList.add(
                ReviewVM(
                    ReviewItem(
                        review.name,
                        review.picture,
                        review.rating,
                        review.date,
                        review.text,
                        image,
                        review.images[0] !is String
                    )
                ).let {
                    val imageList = mutableListOf<RecyclerViewItem>()
                    if (review.images[0] !is String) {
                        review.images.forEach { image ->
                            if (image is Int) {
                                imageList.add(
                                    ReviewImageVM(
                                        ReviewImageItem(
                                            image
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
                            } else if (image is Uri) {
                                imageList.add(
                                    ReviewImageUriVM(
                                        ReviewImageUriItem(
                                            image,
                                            false
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
                        }
                        image.set(imageList)
                    }

                    viewModelScope.launch {
                        it.events().collectLatest { event ->
                            events.emit(event)
                        }
                    }

                    if (it.item.hasPhotos) {
                        newListWithPhotos.add(it.toRecyclerViewItem())
                    }

                    it.toRecyclerViewItem()
                }
            )
        }

        reviewsWithPhotos.set(newListWithPhotos)
        reviewsAll.set(newList)
        data.set(newList)
    }

    sealed class Event {
        object FetchingErrorEvent : Event()
        object WriteReviewEvent : Event()
        object AddPhotoEvent : Event()
        object CloseBottomSheetEvent : Event()
        class ReviewClickEvent(val item: ReviewItem, val position: Int) : Event()
        class RemovePhotoEvent(val item: ReviewImageUriItem, val position: Int) : Event()
    }
}
