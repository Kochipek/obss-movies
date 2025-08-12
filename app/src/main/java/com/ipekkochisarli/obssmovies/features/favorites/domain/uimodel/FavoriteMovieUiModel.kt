package com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel

import com.ipekkochisarli.obssmovies.features.favorites.data.local.entity.FavoriteMovieEntity

enum class LibraryCategoryType {
    WATCHED,
    WATCH_LATER,
}

data class FavoriteMovieUiModel(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val releaseYear: String,
    val listType: LibraryCategoryType,
    val description: String,
)

fun FavoriteMovieUiModel.toEntity(userId: String) =
    FavoriteMovieEntity(
        id = id,
        title = title,
        listType = listType.name,
        userId = userId,
        posterUrl = posterUrl,
        releaseYear = releaseYear,
        description = description,
    )

fun FavoriteMovieEntity.toDomain() =
    FavoriteMovieUiModel(
        id = id,
        title = title,
        posterUrl = posterUrl,
        releaseYear = releaseYear,
        listType = LibraryCategoryType.valueOf(listType),
        description = description,
    )
