package com.ipekkochisarli.obssmovies.features.contentdetail.presentation.adapter.viewholder

import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.databinding.ItemContentDetailHeaderBinding
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.ContentDetailUiModel

class HeaderViewHolder(
    private val binding: ItemContentDetailHeaderBinding,
    private val onActionClicked: ((ContentDetailUiModel, Int) -> Unit)?,
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
