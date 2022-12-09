package com.gd.intership.hdziedziura.androidshopingapp.presentation.ui

import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.adapter.GeneralAdapter
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.SortColorPreferences
import com.google.android.material.slider.RangeSlider

@BindingAdapter("strikeThrough")
fun bindStrikeThrough(textView: AppCompatTextView, strikeThrough: Boolean) {
    if (strikeThrough) {
        textView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        textView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}

@BindingAdapter("rvItems")
fun bindRvItems(recyclerView: RecyclerView, items: List<RecyclerViewItem>) {
    val adapter = recyclerView.adapter
    if (adapter == null) {
        recyclerView.adapter = GeneralAdapter().apply {
            update(items)
        }
    } else if (adapter is GeneralAdapter) {
        adapter.update(items)
    }
}

@BindingAdapter("imageUrl")
fun loadImage(view: AppCompatImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide
            .with(view.context)
            .load(url)
            .placeholder(R.drawable.ic_baseline_broken_image_24)
            .into(view)
    }
}

@BindingAdapter("imageRes")
fun loadImage(view: AppCompatImageView, url: Int) {
    Glide
        .with(view.context)
        .load(url)
        .placeholder(R.drawable.image_top)
        .into(view)
}

@BindingAdapter("imageUri")
fun loadImage(view: AppCompatImageView, uri: Uri) {
    Glide
        .with(view.context)
        .load(uri)
        .placeholder(R.drawable.image_top)
        .into(view)
}

@InverseBindingAdapter(attribute = "rangePrice")
fun getRangeSlider(slider: RangeSlider): List<Float> {
    return slider.values
}

@BindingAdapter("rangePrice")
fun setRangeSlider(slider: RangeSlider, values: List<Float>) {
    if (values == slider.values && values.isNotEmpty()) return
    slider.values = values
}

@BindingAdapter("app:rangePriceAttrChanged")
fun setListeners(
    slider: RangeSlider,
    attrChange: InverseBindingListener
) {
    val listener = RangeSlider.OnChangeListener { _, _, _ -> attrChange.onChange() }
    slider.addOnChangeListener(listener)
}

@InverseBindingAdapter(attribute = "values")
fun getValueFrom(slider: RangeSlider): List<Float> {
    return listOf(slider.valueFrom, slider.valueTo)
}

@BindingAdapter("values")
fun setValueFrom(slider: RangeSlider, values: List<Float>) {
    slider.valueFrom = values[0]
    slider.valueTo = values[1]
}

@BindingAdapter("app:valuesAttrChanged")
fun setListenersValues(
    slider: RangeSlider,
    attrChange: InverseBindingListener
) {
    val listener = RangeSlider.OnChangeListener { _, _, _ -> attrChange.onChange() }
    slider.addOnChangeListener(listener)
}

@BindingAdapter("sortColors")
fun setSortFieldsColors(view: AppCompatTextView, sortColorPreferences: SortColorPreferences) {
    view.apply {
        setTextColor(ContextCompat.getColor(context, sortColorPreferences.textColorId))
        setBackgroundColor(ContextCompat.getColor(context, sortColorPreferences.backgroundColorId))
    }
}
