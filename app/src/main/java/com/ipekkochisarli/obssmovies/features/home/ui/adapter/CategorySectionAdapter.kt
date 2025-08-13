package com.ipekkochisarli.obssmovies.features.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ipekkochisarli.obssmovies.common.MovieViewType
import com.ipekkochisarli.obssmovies.databinding.ItemHomeCategorySectionBinding
import com.ipekkochisarli.obssmovies.features.home.HomeSectionType
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel
import com.ipekkochisarli.obssmovies.features.home.ui.HomeUiState

class CategorySectionAdapter(
    private val showFavoriteIcon: Boolean = false,
    private val onFavoriteClick: ((MovieUiModel) -> Unit)? = null,
    private val onSeeAllClick: ((HomeSectionType) -> Unit)? = null,
    private val onMovieClick: ((MovieUiModel) -> Unit)? = null,
) : RecyclerView.Adapter<CategorySectionAdapter.SectionViewHolder>() {
    private val sections = mutableListOf<HomeUiState>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SectionViewHolder {
        val binding =
            ItemHomeCategorySectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return SectionViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SectionViewHolder,
        position: Int,
    ) {
        holder.bind(sections[position])
    }

    override fun getItemCount(): Int = sections.size

    fun submitList(newSections: List<HomeUiState>) {
        sections.clear()
        sections.addAll(newSections)
        notifyDataSetChanged()
    }

    inner class SectionViewHolder(
        private val binding: ItemHomeCategorySectionBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val movieAdapter =
            MovieListAdapter(
                viewType = MovieViewType.POSTER,
                showFavoriteIcon = showFavoriteIcon,
                onFavoriteClick = onFavoriteClick,
                onMovieClick = onMovieClick,
            )

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
