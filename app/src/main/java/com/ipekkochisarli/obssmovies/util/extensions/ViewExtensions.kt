package com.ipekkochisarli.obssmovies.util.extensions

import android.view.View
import android.widget.ImageView
import coil3.load
import coil3.request.crossfade
import coil3.request.placeholder
import com.ipekkochisarli.obssmovies.R

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visibleIf(condition: Boolean) {
    visibility = if (condition) View.VISIBLE else View.GONE
}

fun ImageView.loadImage(url: String?) {
    this.load(url) {
        crossfade(true)
        placeholder(R.drawable.bg_placeholder)
    }
}
