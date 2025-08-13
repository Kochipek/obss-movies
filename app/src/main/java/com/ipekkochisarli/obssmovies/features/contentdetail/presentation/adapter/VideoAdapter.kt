package com.ipekkochisarli.obssmovies.features.contentdetail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import com.ipekkochisarli.obssmovies.core.base.BaseListAdapter
import com.ipekkochisarli.obssmovies.databinding.ItemVideoBinding
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.VideoUiModel
import com.ipekkochisarli.obssmovies.util.extensions.loadImage

class VideoAdapter(
    private val onVideoClicked: ((videoKey: String) -> Unit)? = null,
) : BaseListAdapter<VideoUiModel>(
        itemsSame = { old, new -> old.id == new.id },
        contentsSame = { old, new -> old == new },
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val binding = ItemVideoBinding.inflate(inflater, parent, false)
        return VideoViewHolder(binding, onVideoClicked)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val video = getItem(position)
        (holder as VideoViewHolder).bind(video)
    }

    class VideoViewHolder(
        private val binding: ItemVideoBinding,
        private val onVideoClicked: ((videoKey: String) -> Unit)?,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(video: VideoUiModel) {
            binding.videoThumbnail.loadImage(video.thumbnailUrl)
            binding.videoName.text = video.name

            binding.root.setOnClickListener {
                onVideoClicked?.invoke(video.videoUrl)
            }
        }
    }
}
