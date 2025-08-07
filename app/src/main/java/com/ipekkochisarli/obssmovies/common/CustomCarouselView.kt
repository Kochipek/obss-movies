package com.ipekkochisarli.obssmovies.common

import android.content.Context
import android.util.AttributeSet
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.privacysandbox.ads.adservices.adid.AdId

data class CarouselItem(
    val id: Int,
    val imageUrl: String,
    val title: String,
    val releaseYear: String,
)

class CustomCarouselView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
    ) : HorizontalScrollView(context, attrs) {
        private val container =
            LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            }

        init {
            isHorizontalScrollBarEnabled = false
            addView(container)
        }
    }
