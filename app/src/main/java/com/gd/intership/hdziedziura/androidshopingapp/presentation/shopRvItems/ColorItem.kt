package com.gd.intership.hdziedziura.androidshopingapp.presentation.shopRvItems

import android.graphics.Color
import androidx.databinding.ObservableBoolean

data class ColorItem(
    var color: Int = Color.BLACK,
    var colorText: String = "",
    var checked: ObservableBoolean = ObservableBoolean(false)
)
