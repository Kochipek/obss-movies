package com.ipekkochisarli.obssmovies.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import coil3.load
import com.ipekkochisarli.obssmovies.databinding.ItemMoviesBinding
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel
import com.ipekkochisarli.obssmovies.util.extensions.gone
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
        ) {
            binding.imagePoster.load(movie.posterUrl)

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
