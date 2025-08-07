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
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.CastUiModel

class CastAdapter :
    BaseListAdapter<CastUiModel>(
        itemsSame = { old, new -> old.id == new.id },
        contentsSame = { old, new -> old == new },
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int,
    ): RecyclerView.ViewHolder = CastViewHolder(inflater.inflate(R.layout.item_cast, parent, false))

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val cast = getItem(position)
        (holder as CastViewHolder).bind(cast)
    }

    class CastViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        private val castImage: ImageView = itemView.findViewById(R.id.castImage)
        private val castName: TextView = itemView.findViewById(R.id.castName)

        fun bind(cast: CastUiModel) {
            castImage.load(cast.profileImageUrl) {
                crossfade(true)
            }
            castName.text = cast.name
        }
    }
}
