package com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.views

import android.content.Context
import android.util.AttributeSet
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.google.android.material.textfield.TextInputLayout

class TextInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {

    override fun setErrorEnabled(enabled: Boolean) {
        super.setErrorEnabled(enabled)
        if (enabled) {
            this.editText?.setBackgroundResource(R.drawable.text_field_background_error)
        } else {
            this.editText?.setBackgroundResource(R.drawable.text_field_background_normal)
        }
    }
}
