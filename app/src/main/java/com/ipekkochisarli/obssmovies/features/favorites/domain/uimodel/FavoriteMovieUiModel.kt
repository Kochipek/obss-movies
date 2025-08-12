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
)

fun FavoriteMovieUiModel.toEntity(userId: String) =
    FavoriteMovieEntity(
        id = this.id,
        title = this.title,
        listType = this.listType.name,
        userId = userId,
        posterUrl = this.posterUrl,
        releaseYear = this.releaseYear,
    )

fun FavoriteMovieEntity.toDomain() =
    FavoriteMovieUiModel(
        id = id,
        title = title,
        posterUrl = posterUrl,
        releaseYear = releaseYear,
        listType = LibraryCategoryType.valueOf(listType),
    )
