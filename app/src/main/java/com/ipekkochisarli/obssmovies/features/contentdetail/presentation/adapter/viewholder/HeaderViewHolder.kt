package com.ipekkochisarli.obssmovies.features.contentdetail.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.databinding.ItemContentDetailHeaderBinding
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.ContentDetailUiModel

class HeaderViewHolder(
    private val binding: ItemContentDetailHeaderBinding,
    private val onShareClicked: ((String) -> Unit)? = null,
    private val onWatchLaterClicked: (() -> Unit)? = null,
    private val onWatchedClicked: (() -> Unit)? = null,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        detail: ContentDetailUiModel,
        isAddedWatchLater: Boolean,
        isWatched: Boolean,
    ) = with(binding) {
        val context = root.context

        posterImage.load(detail.posterUrl) {
            crossfade(true)
        }

        backgroundPosterImage.load(detail.backdropUrl) {
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
        shareButton.setOnClickListener {
            detail.title.let { title ->
                onShareClicked?.invoke(title)
            }
        }
        btnWatchLater.setImageResource(
            if (isAddedWatchLater) R.drawable.ic_tick else R.drawable.ic_plus,
        )
        btnWatched.setImageResource(
            if (isWatched) R.drawable.ic_eye_closed else R.drawable.ic_eye_open,
        )

        btnWatchLater.setOnClickListener {
            onWatchLaterClicked?.invoke()
        }
        btnWatched.setOnClickListener {
            onWatchedClicked?.invoke()
        }

        shareButton.setOnClickListener {
            onShareClicked?.invoke(detail.title)
        }
    }
}
