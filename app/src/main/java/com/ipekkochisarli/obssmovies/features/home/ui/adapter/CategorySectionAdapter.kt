package com.ipekkochisarli.obssmovies.features.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ipekkochisarli.obssmovies.databinding.ItemHomeCategorySectionBinding
import com.ipekkochisarli.obssmovies.features.home.HomeSectionType
import com.ipekkochisarli.obssmovies.features.home.ui.HomeUiState

class CategorySectionAdapter(
    private var sections: List<HomeUiState>,
    private val onSeeAllClick: ((HomeSectionType) -> Unit)? = null,
) : RecyclerView.Adapter<CategorySectionAdapter.SectionViewHolder>() {
    inner class SectionViewHolder(
        private val binding: ItemHomeCategorySectionBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val movieAdapter = MovieListAdapter(emptyList())

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
            movieAdapter.updateMovies(section.movies.take(5))

            binding.textSeeAll.setOnClickListener {
                onSeeAllClick?.invoke(section.type)
            }
        }
    }

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

    fun updateSections(newSections: List<HomeUiState>) {
        sections = newSections
        notifyDataSetChanged()
    }
}
