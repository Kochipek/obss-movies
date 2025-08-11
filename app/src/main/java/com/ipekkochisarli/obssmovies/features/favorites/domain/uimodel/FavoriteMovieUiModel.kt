package com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel

import com.ipekkochisarli.obssmovies.features.favorites.data.local.entity.FavoriteMovieEntity

enum class FavoriteListType {
    WATCHED,
    WATCH_LATER,
}

data class FavoriteMovieUiModel(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val releaseYear: String,
    val listType: FavoriteListType,
)

fun FavoriteMovieUiModel.toEntity() =
    FavoriteMovieEntity(
        id = id,
        title = title,
        posterUrl = posterUrl,
        releaseYear = releaseYear,
        listType = listType.name,
    )

fun FavoriteMovieEntity.toDomain() =
    FavoriteMovieUiModel(
        id = id,
        title = title,
        posterUrl = posterUrl,
        releaseYear = releaseYear,
        listType = FavoriteListType.valueOf(listType),
    )
