package com.ipekkochisarli.obssmovies.util.extensions

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.common.MovieViewType

fun RecyclerView.updateLayoutManagerWithState(
    viewType: MovieViewType,
    toggleButton: androidx.appcompat.widget.AppCompatImageView,
) {
    val state = layoutManager?.onSaveInstanceState()

    layoutManager =
        when (viewType) {
            MovieViewType.LIST -> LinearLayoutManager(context)
            MovieViewType.GRID -> {
                val spanCount = calculateSpanCount()
                GridLayoutManager(context, spanCount)
            }

            else -> LinearLayoutManager(context)
        }

    layoutManager?.onRestoreInstanceState(state)

    toggleButton.setImageResource(
        if (viewType == MovieViewType.LIST) R.drawable.ic_grid else R.drawable.ic_list,
    )
}

private fun RecyclerView.calculateSpanCount(): Int {
    val displayMetrics = context.resources.displayMetrics
    val screenWidthPx = displayMetrics.widthPixels
    val density = displayMetrics.density
    val itemWidthDp = 100
    val itemWidthPx = (itemWidthDp * density).toInt()

    return maxOf(1, screenWidthPx / itemWidthPx)
}
