package com.ipekkochisarli.obssmovies.features.contentdetail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ipekkochisarli.obssmovies.core.base.BaseListAdapter
import com.ipekkochisarli.obssmovies.databinding.ItemCastBinding
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.CastUiModel
import com.ipekkochisarli.obssmovies.util.extensions.loadImage

class CastAdapter(
    private val onCastClick: ((Int) -> Unit)? = null,
) : BaseListAdapter<CastUiModel>(
        itemsSame = { old, new -> old.id == new.id },
        contentsSame = { old, new -> old == new },
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val binding = ItemCastBinding.inflate(inflater, parent, false)
        return CastViewHolder(binding, onCastClick)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val cast = getItem(position)
        (holder as CastViewHolder).bind(cast)
    }

    class CastViewHolder(
        private val binding: ItemCastBinding,
        private val onCastClick: ((Int) -> Unit)?,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cast: CastUiModel) {
            binding.castImage.loadImage(cast.profileImageUrl)
            binding.castName.text = cast.name

            binding.root.setOnClickListener {
                onCastClick?.invoke(cast.id)
            }
        }
    }
}
