package com.ipekkochisarli.obssmovies.features.contentdetail.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.core.base.BaseListAdapter
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel
import com.ipekkochisarli.obssmovies.util.extensions.loadImage

class SimilarMoviesAdapter(
    private val onItemClick: ((MovieUiModel) -> Unit)? = null,
) : BaseListAdapter<MovieUiModel>(
        itemsSame = { old, new -> old.id == new.id },
        contentsSame = { old, new -> old == new },
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int,
    ): RecyclerView.ViewHolder = SimilarMovieViewHolder(inflater.inflate(R.layout.item_similar_movies, parent, false))

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val movie = getItem(position)
        (holder as SimilarMovieViewHolder).bind(movie)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(movie)
        }
    }

    class SimilarMovieViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        private val poster: ImageView = itemView.findViewById(R.id.moviePoster)
        private val title: TextView = itemView.findViewById(R.id.movieTitle)

        fun bind(movie: MovieUiModel) {
            poster.loadImage(movie.posterUrl)
            title.text = movie.title
        }
    }
}
