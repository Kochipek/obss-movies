package com.ipekkochisarli.obssmovies.features.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ipekkochisarli.obssmovies.common.MovieItemView
import com.ipekkochisarli.obssmovies.common.MovieViewType
import com.ipekkochisarli.obssmovies.core.base.BaseListAdapter
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel

class MovieListAdapter(
    private var viewType: MovieViewType = MovieViewType.LIST,
) : BaseListAdapter<MovieUiModel>(
        itemsSame = { old, new -> old.id == new.id },
        contentsSame = { old, new -> old == new },
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int,
    ): MovieViewHolder {
        val view = MovieItemView(parent.context)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val movie = getItem(position)
        (holder as MovieViewHolder).movieItemView.bind(movie, viewType)
    }

    inner class MovieViewHolder(
        val movieItemView: MovieItemView,
    ) : RecyclerView.ViewHolder(movieItemView)

    fun setViewType(type: MovieViewType) {
        if (viewType != type) {
            viewType = type
            notifyItemRangeChanged(0, itemCount)
        }
    }
}
