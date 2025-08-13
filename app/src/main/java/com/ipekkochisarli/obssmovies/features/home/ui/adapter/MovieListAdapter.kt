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
    private val onMovieClick: ((MovieUiModel) -> Unit)? = null,
    private val onFavoriteClick: ((MovieUiModel) -> Unit)? = null,
    private val showFavoriteIcon: Boolean = false,
) : BaseListAdapter<MovieUiModel>(
        itemsSame = { old, new -> old.id == new.id },
        contentsSame = { old, new -> old == new },
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val movieItemView = MovieItemView(parent.context)
        return MovieViewHolder(movieItemView)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val movie = getItem(position)
        (holder as MovieViewHolder).movieItemView.bind(
            movie,
            viewType,
            onFavoriteClick = { onFavoriteClick?.invoke(it) },
            showFavoriteIcon = showFavoriteIcon,
        )
        holder.itemView.setOnClickListener {
            onMovieClick?.invoke(movie)
        }
    }

    fun setViewType(type: MovieViewType) {
        viewType = type
    }

    inner class MovieViewHolder(
        val movieItemView: MovieItemView,
    ) : RecyclerView.ViewHolder(movieItemView)
}
