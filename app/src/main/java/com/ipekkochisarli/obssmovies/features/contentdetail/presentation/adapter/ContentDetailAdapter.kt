package com.ipekkochisarli.obssmovies.features.contentdetail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ipekkochisarli.obssmovies.databinding.ItemContentDetailHeaderBinding
import com.ipekkochisarli.obssmovies.databinding.ItemContentDetailSectionBinding
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.CastUiModel
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.ContentDetailUiModel
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.VideoUiModel
import com.ipekkochisarli.obssmovies.features.contentdetail.presentation.adapter.viewholder.CastSectionViewHolder
import com.ipekkochisarli.obssmovies.features.contentdetail.presentation.adapter.viewholder.HeaderViewHolder
import com.ipekkochisarli.obssmovies.features.contentdetail.presentation.adapter.viewholder.SimilarMoviesSectionViewHolder
import com.ipekkochisarli.obssmovies.features.contentdetail.presentation.adapter.viewholder.VideoSectionViewHolder
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

    var onShareClicked: ((String) -> Unit)? = null
    var onVideoClicked: ((String) -> Unit)? = null
    var onSimilarMovieClick: ((MovieUiModel) -> Unit)? = null

    var onWatchLaterClicked: (() -> Unit)? = null
    var onWatchedClicked: (() -> Unit)? = null

    var isAddedWatchLater = false
    var isWatched = false

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_CAST = 1
        private const val TYPE_VIDEOS = 2
        private const val TYPE_SIMILAR = 3
    }

    fun submitList(
        newItems: List<ContentDetailItem>,
        watchLater: Boolean,
        watched: Boolean,
    ) {
        items.clear()
        items.addAll(newItems)
        isAddedWatchLater = watchLater
        isWatched = watched
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

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
                HeaderViewHolder(binding, onShareClicked, onWatchLaterClicked, onWatchedClicked)
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
                VideoSectionViewHolder(binding) { videoUrl ->
                    onVideoClicked?.invoke(videoUrl)
                }
            }

            TYPE_SIMILAR -> {
                val binding =
                    ItemContentDetailSectionBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                SimilarMoviesSectionViewHolder(binding, onSimilarMovieClick)
            }

            else -> throw IllegalArgumentException("Unknown viewType $viewType")
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val item = items[position]) {
            is ContentDetailItem.Header ->
                (holder as HeaderViewHolder).bind(
                    item.detail,
                    isAddedWatchLater,
                    isWatched,
                )

            is ContentDetailItem.SectionCast -> (holder as CastSectionViewHolder).bind(item.castList)
            is ContentDetailItem.SectionVideos -> (holder as VideoSectionViewHolder).bind(item.videoList)
            is ContentDetailItem.SectionSimilarMovies ->
                (holder as SimilarMoviesSectionViewHolder).bind(
                    item.movieList,
                )
        }
    }
}
