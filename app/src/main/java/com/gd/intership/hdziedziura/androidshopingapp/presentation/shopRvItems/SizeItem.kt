package com.gd.intership.hdziedziura.androidshopingapp.presentation.shopRvItems

import androidx.databinding.ObservableBoolean

data class SizeItem(
    var size: String = "",
    var checked: ObservableBoolean = ObservableBoolean(false)
)
