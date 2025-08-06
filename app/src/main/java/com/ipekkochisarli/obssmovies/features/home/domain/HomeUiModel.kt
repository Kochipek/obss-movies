package com.ipekkochisarli.obssmovies.features.home.domain

import android.os.Parcelable
import com.ipekkochisarli.obssmovies.common.MovieViewType
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieUiModel(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val releaseYear: String,
    val voteAverage: String,
    val carouselUrl: String? = null,
    val description: String? = null,
    val viewType: MovieViewType? = null,
) : Parcelable
