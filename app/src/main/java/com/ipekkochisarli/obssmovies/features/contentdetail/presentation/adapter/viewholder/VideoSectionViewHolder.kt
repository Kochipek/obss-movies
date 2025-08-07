package com.ipekkochisarli.obssmovies.features.contentdetail.presentation.adapter.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ipekkochisarli.obssmovies.databinding.ItemContentDetailSectionBinding
import com.ipekkochisarli.obssmovies.features.contentdetail.DetailSectionType
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.VideoUiModel
import com.ipekkochisarli.obssmovies.features.contentdetail.presentation.adapter.VideoAdapter

class VideoSectionViewHolder(
    private val binding: ItemContentDetailSectionBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private val adapter = VideoAdapter()

    init {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = adapter
    }

    fun bind(videoList: List<VideoUiModel>) {
        binding.sectionTitle.text =
            binding.root.context.getString(DetailSectionType.VIDEOS.titleResId)
        adapter.submitList(videoList)
    }
}
