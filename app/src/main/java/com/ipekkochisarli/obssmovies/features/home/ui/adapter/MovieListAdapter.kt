package com.ipekkochisarli.obssmovies.features.home.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ipekkochisarli.obssmovies.common.MovieItemView
import com.ipekkochisarli.obssmovies.common.MovieViewType
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel

class MovieListAdapter(
    private var movies: List<MovieUiModel>,
    private var viewType: MovieViewType = MovieViewType.LIST,
) : RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {
    // todo can be relpaced with list adapter later

    inner class MovieViewHolder(
        val movieItemView: MovieItemView,
    ) : RecyclerView.ViewHolder(movieItemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MovieViewHolder {
        val view = MovieItemView(parent.context)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int,
    ) {
        val movie = movies[position]
        holder.movieItemView.bind(movie, viewType)
    }

    override fun getItemCount(): Int = movies.size

    fun updateMovies(newMovies: List<MovieUiModel>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    fun setViewType(type: MovieViewType) {
        viewType = type
        notifyDataSetChanged()
    }
}
