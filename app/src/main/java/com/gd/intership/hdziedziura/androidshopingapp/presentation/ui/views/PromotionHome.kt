package com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.gd.intership.hdziedziura.androidshopingapp.R

class PromotionHome @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        inflate(context, R.layout.item_home_grid, this)

        val textTop = findViewById<AppCompatTextView>(R.id.text_top)
        val textRight = findViewById<AppCompatTextView>(R.id.text_right)
        val textBottom = findViewById<AppCompatTextView>(R.id.text_bottom)
        val textCenter = findViewById<AppCompatTextView>(R.id.text_center)

        val imgTop = findViewById<AppCompatImageView>(R.id.img_top)
        val imgRight = findViewById<AppCompatImageView>(R.id.img_right)
        val imgBottom = findViewById<AppCompatImageView>(R.id.img_bottom)
        val imgCenter = findViewById<AppCompatImageView>(R.id.img_center)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.PromotionHome)

        try {
            val text1 = ta.getString(R.styleable.PromotionHome_text_top)
            val text2 = ta.getString(R.styleable.PromotionHome_text_right)
            val text3 = ta.getString(R.styleable.PromotionHome_text_bottom)
            val text4 = ta.getString(R.styleable.PromotionHome_text_center)

            val img1 = ta.getResourceId(R.styleable.PromotionHome_img_top, 0)
            if (img1 != 0) {
                val img = AppCompatResources.getDrawable(context, img1)
                imgTop.setImageDrawable(img)
            }

            val img2 = ta.getColor(R.styleable.PromotionHome_img_right, 0)
            if (img2 != 0) {
                val img = AppCompatResources.getDrawable(context, img2)
                imgRight.setImageDrawable(img)
            }

            val img3 = ta.getColor(R.styleable.PromotionHome_img_bottom, 0)
            if (img3 != 0) {
                val img = AppCompatResources.getDrawable(context, img3)
                imgBottom.setImageDrawable(img)
            }

            val img4 = ta.getColor(R.styleable.PromotionHome_img_center, 0)
            if (img4 != 0) {
                val img = AppCompatResources.getDrawable(context, img4)
                imgCenter.setImageDrawable(img)
            }

            textTop.text = text1
            textRight.text = text2
            textBottom.text = text3
            textCenter.text = text4
        } finally {
            ta.recycle()
        }
    }
}
