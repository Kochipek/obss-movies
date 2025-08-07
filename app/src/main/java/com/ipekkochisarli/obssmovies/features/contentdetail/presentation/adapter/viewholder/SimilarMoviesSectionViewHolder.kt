package com.ipekkochisarli.obssmovies.features.contentdetail.presentation.adapter.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ipekkochisarli.obssmovies.databinding.ItemContentDetailSectionBinding
import com.ipekkochisarli.obssmovies.features.contentdetail.DetailSectionType
import com.ipekkochisarli.obssmovies.features.contentdetail.presentation.adapter.SimilarMoviesAdapter
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel

class SimilarMoviesSectionViewHolder(
    private val binding: ItemContentDetailSectionBinding,
    private val onItemClick: ((MovieUiModel) -> Unit)? = null,
) : RecyclerView.ViewHolder(binding.root) {
    private val adapter = SimilarMoviesAdapter(onItemClick)

    init {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = adapter
    }

    fun bind(movieList: List<MovieUiModel>) {
        binding.sectionTitle.text = binding.root.context.getString(DetailSectionType.SIMILAR_MOVIES.titleResId)
        adapter.submitList(movieList)
    }
}
