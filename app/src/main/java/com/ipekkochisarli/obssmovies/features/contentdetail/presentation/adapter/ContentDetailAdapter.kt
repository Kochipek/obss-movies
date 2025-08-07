package com.ipekkochisarli.obssmovies.features.contentdetail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.databinding.ItemContentDetailHeaderBinding
import com.ipekkochisarli.obssmovies.databinding.ItemContentDetailSectionBinding
import com.ipekkochisarli.obssmovies.features.contentdetail.DetailSectionType
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.CastUiModel
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.ContentDetailUiModel
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.VideoUiModel
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel

sealed class ContentDetailItem {
    data class Header(
        val detail: ContentDetailUiModel,
    ) : ContentDetailItem()

    data class SectionCast(
        val castList: List<CastUiModel>,
    ) : ContentDetailItem()

    data class SectionVideos(
        val videoList: List<VideoUiModel>,
    ) : ContentDetailItem()

    data class SectionSimilarMovies(
        val movieList: List<MovieUiModel>,
    ) : ContentDetailItem()
}

class ContentDetailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<ContentDetailItem>()

    var onActionClicked: ((ContentDetailUiModel, Int) -> Unit)? = null

    fun submitList(newItems: List<ContentDetailItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_CAST = 1
        private const val TYPE_VIDEOS = 2
        private const val TYPE_SIMILAR = 3
    }

    override fun getItemViewType(position: Int): Int =
        when (items[position]) {
            is ContentDetailItem.Header -> TYPE_HEADER
            is ContentDetailItem.SectionCast -> TYPE_CAST
            is ContentDetailItem.SectionVideos -> TYPE_VIDEOS
            is ContentDetailItem.SectionSimilarMovies -> TYPE_SIMILAR
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        when (viewType) {
            TYPE_HEADER -> {
                val binding =
                    ItemContentDetailHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                HeaderViewHolder(binding)
            }

            TYPE_CAST -> {
                val binding =
                    ItemContentDetailSectionBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                CastSectionViewHolder(binding)
            }

            TYPE_VIDEOS -> {
                val binding =
                    ItemContentDetailSectionBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                VideoSectionViewHolder(binding)
            }

            TYPE_SIMILAR -> {
                val binding =
                    ItemContentDetailSectionBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                SimilarMoviesSectionViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Unknown viewType $viewType")
        }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val item = items[position]) {
            is ContentDetailItem.Header -> (holder as HeaderViewHolder).bind(item.detail)
            is ContentDetailItem.SectionCast -> (holder as CastSectionViewHolder).bind(item.castList)
            is ContentDetailItem.SectionVideos -> (holder as VideoSectionViewHolder).bind(item.videoList)
            is ContentDetailItem.SectionSimilarMovies ->
                (holder as SimilarMoviesSectionViewHolder).bind(
                    item.movieList,
                )
        }
    }

    inner class HeaderViewHolder(
        private val binding: ItemContentDetailHeaderBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(detail: ContentDetailUiModel) {
            val context = binding.root.context
            binding.posterImage.load(detail.posterUrl) {
                crossfade(true)
            }
            binding.titleText.text = detail.title
            binding.taglineText.text = detail.tagline.takeIf { it.isNotBlank() } ?: ""
            binding.overviewText.text = detail.overview

            binding.runtimeText.text = detail.runtimeMinutes?.let {
                context.getString(R.string.runtime_format, it)
            } ?: context.getString(R.string.runtime_unknown)

            binding.statusText.text = context.getString(R.string.status_format, detail.status)
            binding.productionCompaniesText.text =
                context.getString(R.string.production_companies_format, detail.productionCompanies)
            binding.infoText.text =
                context.getString(R.string.info_format, detail.releaseYear, detail.genres)
            binding.ratingText.text = context.getString(R.string.rating_format, detail.rating)

            binding.actionButton.setOnClickListener { view ->
                val popup = PopupMenu(view.context, view)
                popup.menuInflater.inflate(R.menu.content_details_dropdown, popup.menu)
                popup.setOnMenuItemClickListener { menuItem ->
                    onActionClicked?.invoke(detail, menuItem.itemId)
                    true
                }
                popup.show()
            }
        }
    }

    inner class CastSectionViewHolder(
        private val binding: ItemContentDetailSectionBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val adapter = CastAdapter()

        init {
            binding.recyclerView.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerView.adapter = adapter
        }

        fun bind(castList: List<CastUiModel>) {
            binding.sectionTitle.text = binding.root.context.getString(DetailSectionType.CREDITS.titleResId)
            adapter.submitList(castList)
        }
    }

    inner class VideoSectionViewHolder(
        private val binding: ItemContentDetailSectionBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val adapter = VideoAdapter()

        init {
            binding.recyclerView.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerView.adapter = adapter
        }

        fun bind(videoList: List<VideoUiModel>) {
            binding.sectionTitle.text = binding.root.context.getString(DetailSectionType.VIDEOS.titleResId)
            adapter.submitList(videoList)
        }
    }

    inner class SimilarMoviesSectionViewHolder(
        private val binding: ItemContentDetailSectionBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val adapter = SimilarMoviesAdapter()

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
}
