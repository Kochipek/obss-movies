package com.ipekkochisarli.obssmovies.features.contentdetail.domain

data class ContentDetailUiModel(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseYear: String,
    val genres: String,
    val rating: String,
    val posterUrl: String,
    val backdropUrl: String,
    val runtimeMinutes: Int? = null,
    val tagline: String = "",
    val status: String = "",
    val originalLanguage: String = "",
    val budget: Int? = null,
    val revenue: Int? = null,
    val homepage: String = "",
    val productionCompanies: String = "",
)

data class CastUiModel(
    val id: Int,
    val name: String,
    val character: String,
    val profileImageUrl: String,
)

data class VideoUiModel(
    val id: String,
    val name: String,
    val thumbnailUrl: String,
    val videoUrl: String,
)
