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
    fun bind(detail: ContentDetailUiModel) =
        with(binding) {
            val context = root.context

            posterImage.load(detail.posterUrl) {
                crossfade(true)
            }

            titleText.text = detail.title
            taglineText.text = detail.tagline.takeIf { it.isNotBlank() } ?: ""
            overviewText.text = detail.overview

            runtimeText.text = detail.runtimeMinutes?.let {
                context.getString(R.string.runtime_format, it)
            } ?: context.getString(R.string.runtime_unknown)

            statusText.text = context.getString(R.string.status_format, detail.status)
            productionCompaniesText.text =
                context.getString(R.string.production_companies_format, detail.productionCompanies)

            infoText.text =
                context.getString(R.string.info_format, detail.releaseYear, detail.genres)

            ratingText.text = context.getString(R.string.rating_format, detail.rating)

            actionButton.setOnClickListener { view ->
                PopupMenu(view.context, view).apply {
                    menuInflater.inflate(R.menu.content_details_dropdown, menu)
                    setOnMenuItemClickListener { menuItem ->
                        onActionClicked?.invoke(detail, menuItem.itemId)
                        true
                    }
                    show()
                }
            }
        }
}
