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
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.VideoUiModel

class VideoAdapter :
    BaseListAdapter<VideoUiModel>(
        itemsSame = { old, new -> old.id == new.id },
        contentsSame = { old, new -> old == new },
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int,
    ): RecyclerView.ViewHolder = VideoViewHolder(inflater.inflate(R.layout.item_video, parent, false))

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val video = getItem(position)
        (holder as VideoViewHolder).bind(video)
    }

    class VideoViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        private val thumbnail: ImageView = itemView.findViewById(R.id.videoThumbnail)
        private val videoName: TextView = itemView.findViewById(R.id.videoName)

        fun bind(video: VideoUiModel) {
            thumbnail.load(video.thumbnailUrl) {
                crossfade(true)
            }
            videoName.text = video.name
        }
    }
}
