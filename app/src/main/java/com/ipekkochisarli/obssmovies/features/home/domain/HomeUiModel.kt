package com.ipekkochisarli.obssmovies.features.home.domain

data class MovieUiModel(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val releaseYear: String,
    val voteAverage: String,
    val carouselUrl: String? = null,
)
