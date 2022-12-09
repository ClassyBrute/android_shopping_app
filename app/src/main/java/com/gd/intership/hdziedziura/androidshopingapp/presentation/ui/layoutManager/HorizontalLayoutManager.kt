package com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.layoutManager

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager

class HorizontalLayoutManager(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayoutManager(context, attrs, defStyleAttr, defStyleRes) {
    init {
        orientation = HORIZONTAL
    }
}
