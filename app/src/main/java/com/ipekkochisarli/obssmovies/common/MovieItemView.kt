package com.ipekkochisarli.obssmovies.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.databinding.ItemMoviesBinding
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel
import com.ipekkochisarli.obssmovies.util.extensions.gone
import com.ipekkochisarli.obssmovies.util.extensions.loadImage
import com.ipekkochisarli.obssmovies.util.extensions.visible

enum class MovieViewType {
    LIST,
    GRID,
    POSTER,
}

class MovieItemView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
    ) : FrameLayout(context, attrs, defStyle) {
        private val binding: ItemMoviesBinding =
            ItemMoviesBinding.inflate(LayoutInflater.from(context), this, true)

        fun bind(
            movie: MovieUiModel,
            viewType: MovieViewType,
            showFavoriteIcon: Boolean = true,
            onFavoriteClick: ((MovieUiModel) -> Unit)? = null,
        ) {
            loadPoster(movie.posterUrl)
            updateFavoriteIcon(movie.isAddedWatchLater, showFavoriteIcon, movie, onFavoriteClick)
            updateTextViews(movie, viewType)
        }

        private fun loadPoster(url: String?) {
            binding.imagePoster.loadImage(url)
        }

        private fun updateFavoriteIcon(
            isAdded: Boolean,
            show: Boolean,
            movie: MovieUiModel,
            onClick: ((MovieUiModel) -> Unit)?,
        ) {
            if (!show) {
                binding.imageFavorite.gone()
                return
            }

            binding.imageFavorite.visible()
            binding.imageFavorite.setImageResource(if (isAdded) R.drawable.ic_tick else R.drawable.ic_plus)
            binding.imageFavorite.setOnClickListener { onClick?.invoke(movie) }
        }

        private fun updateTextViews(
            movie: MovieUiModel,
            viewType: MovieViewType,
        ) {
            when (viewType) {
                MovieViewType.LIST -> {
                    binding.textTitle.visible()
                    binding.textSubtitle.visible()
                    binding.textTitle.text = movie.title
                    binding.textSubtitle.text = movie.description
                }

                MovieViewType.GRID -> {
                    binding.textTitle.gone()
                    binding.textSubtitle.gone()
                }

                MovieViewType.POSTER -> {
                    binding.textTitle.gone()
                    binding.textSubtitle.gone()
                }
            }
        }
    }
