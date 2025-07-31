package com.ipekkochisarli.obssmovies.features.movie.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.ipekkochisarli.obssmovies.databinding.ItemMoviesBinding
import com.ipekkochisarli.obssmovies.features.movie.domain.MovieUiModel

class MovieListAdapter(
    private val movies: List<MovieUiModel>,
) : RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {
    // todo can be relpaced with list adapter later

    inner class MovieViewHolder(
        val binding: ItemMoviesBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MovieViewHolder {
        val binding = ItemMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int,
    ) {
        val movie = movies[position]
        holder.binding.apply {
            textTitle.text = movie.title
            imagePoster.load(movie.posterUrl) {
                // Todo placeholder, error handling
            }
        }
    }

    override fun getItemCount(): Int = movies.size
}
