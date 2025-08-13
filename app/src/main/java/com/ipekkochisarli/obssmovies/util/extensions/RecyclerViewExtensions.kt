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
            MovieViewType.GRID -> GridLayoutManager(context, 3)
            else -> LinearLayoutManager(context)
        }

    layoutManager?.onRestoreInstanceState(state)

    toggleButton.setImageResource(
        if (viewType == MovieViewType.LIST) R.drawable.ic_grid else R.drawable.ic_list,
    )
}
