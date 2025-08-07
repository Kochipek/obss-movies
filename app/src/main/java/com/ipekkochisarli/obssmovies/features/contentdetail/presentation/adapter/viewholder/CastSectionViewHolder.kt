package com.ipekkochisarli.obssmovies.features.contentdetail.presentation.adapter.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ipekkochisarli.obssmovies.databinding.ItemContentDetailSectionBinding
import com.ipekkochisarli.obssmovies.features.contentdetail.DetailSectionType
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.CastUiModel
import com.ipekkochisarli.obssmovies.features.contentdetail.presentation.adapter.CastAdapter

class CastSectionViewHolder(
    private val binding: ItemContentDetailSectionBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private val adapter = CastAdapter()

    init {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = adapter
    }

    fun bind(castList: List<CastUiModel>) {
        binding.sectionTitle.text =
            binding.root.context.getString(DetailSectionType.CREDITS.titleResId)
        adapter.submitList(castList)
    }
}
