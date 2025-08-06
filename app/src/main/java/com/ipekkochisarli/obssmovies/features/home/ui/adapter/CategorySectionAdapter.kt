package com.ipekkochisarli.obssmovies.features.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ipekkochisarli.obssmovies.common.MovieViewType
import com.ipekkochisarli.obssmovies.core.base.BaseListAdapter
import com.ipekkochisarli.obssmovies.databinding.ItemHomeCategorySectionBinding
import com.ipekkochisarli.obssmovies.features.home.HomeSectionType
import com.ipekkochisarli.obssmovies.features.home.ui.HomeUiState

class CategorySectionAdapter(
    private val onSeeAllClick: ((HomeSectionType) -> Unit)? = null,
) : BaseListAdapter<HomeUiState>(
        itemsSame = { old, new -> old.type == new.type },
        contentsSame = { old, new -> old == new },
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int,
    ): SectionViewHolder {
        val binding = ItemHomeCategorySectionBinding.inflate(inflater, parent, false)
        return SectionViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        (holder as SectionViewHolder).bind(getItem(position))
    }

    inner class SectionViewHolder(
        private val binding: ItemHomeCategorySectionBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val movieAdapter = MovieListAdapter(MovieViewType.POSTER)

        init {
            binding.recyclerViewMoviesHorizontal.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = movieAdapter
                setHasFixedSize(true)
                isNestedScrollingEnabled = false
            }
        }

        fun bind(section: HomeUiState) {
            binding.textSectionTitle.text = section.title.replace("_", " ")
            movieAdapter.submitList(section.movies.take(5))

            binding.textSeeAll.setOnClickListener {
                onSeeAllClick?.invoke(section.type)
            }
        }
    }
}
