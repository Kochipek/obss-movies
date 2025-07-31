package com.ipekkochisarli.obssmovies.features.movie.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.ipekkochisarli.obssmovies.features.movie.domain.MovieUiModel
import com.ipekkochisarli.obssmovies.util.Constants

data class MovieResponse(
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("total_pages")
    val totalPages: Int? = null,
    @SerializedName("results")
    val results: List<MovieResultsItem?>? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null,
)

data class MovieResultsItem(
    @SerializedName("overview")
    val overview: String? = null,
    @SerializedName("original_language")
    val originalLanguage: String? = null,
    @SerializedName("original_title")
    val originalTitle: String? = null,
    @SerializedName("video")
    val video: Boolean? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("genre_ids")
    val genreIds: List<Int?>? = null,
    @SerializedName("poster_path")
    val posterPath: String? = null,
    @SerializedName("backdrop_path")
    val backdropPath: String? = null,
    @SerializedName("release_date")
    val releaseDate: String? = null,
    @SerializedName("popularity")
    val popularity: Any? = null,
    @SerializedName("vote_average")
    val voteAverage: Any? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("adult")
    val adult: Boolean? = null,
    @SerializedName("vote_count")
    val voteCount: Int? = null,
)

fun MovieResultsItem.toDomain(): MovieUiModel? {
    val id = this.id ?: return null
    val title = this.title.orEmpty()
    val posterUrl =
        if (!posterPath.isNullOrBlank()) {
            "${Constants.TMDB_IMAGE_BASE_URL}$posterPath"
        } else {
            ""
        }
    val releaseYear = this.releaseDate?.take(4).orEmpty()
    val voteAvg = voteAverage.toString()

    return MovieUiModel(
        id = id,
        title = title,
        posterUrl = posterUrl,
        releaseYear = releaseYear,
        voteAverage = voteAvg,
    )
}
