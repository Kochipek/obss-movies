package com.ipekkochisarli.obssmovies.features.movielist

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.ipekkochisarli.obssmovies.common.MovieItemView
import com.ipekkochisarli.obssmovies.common.MovieViewType
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel

class MoviePagingAdapter(
    private var viewType: MovieViewType = MovieViewType.LIST,
    private val onMovieClick: ((MovieUiModel) -> Unit)? = null,
    private val onFavoriteClick: ((MovieUiModel) -> Unit)? = null,
    private val showFavoriteIcon: Boolean = false,
) : PagingDataAdapter<MovieUiModel, MoviePagingAdapter.MovieViewHolder>(MOVIE_DIFF_CALLBACK) {
    companion object {
        private val MOVIE_DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<MovieUiModel>() {
                override fun areItemsTheSame(
                    oldItem: MovieUiModel,
                    newItem: MovieUiModel,
                ) = oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: MovieUiModel,
                    newItem: MovieUiModel,
                ) = oldItem == newItem
            }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MovieViewHolder {
        val movieItemView = MovieItemView(parent.context)
        return MovieViewHolder(movieItemView)
    }

    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int,
    ) {
        val movie = getItem(position) ?: return
        holder.movieItemView.bind(
            movie,
            viewType,
            onFavoriteClick = { onFavoriteClick?.invoke(it) },
            showFavoriteIcon = showFavoriteIcon,
        )
        holder.itemView.setOnClickListener { onMovieClick?.invoke(movie) }
    }

    fun setViewType(type: MovieViewType) {
        viewType = type
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(
        val movieItemView: MovieItemView,
    ) : androidx.recyclerview.widget.RecyclerView.ViewHolder(movieItemView)
}
