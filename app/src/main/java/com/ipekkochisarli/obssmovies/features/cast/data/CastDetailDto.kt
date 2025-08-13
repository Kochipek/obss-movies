package com.ipekkochisarli.obssmovies.features.cast.data

import com.google.gson.annotations.SerializedName
import com.ipekkochisarli.obssmovies.features.cast.domain.CastUiModel

data class CastDetailDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("biography")
    val biography: String?,
    @SerializedName("birthday")
    val birthday: String?,
    @SerializedName("deathday")
    val deathday: String?,
    @SerializedName("profile_path")
    val profilePath: String?,
    @SerializedName("place_of_birth")
    val placeOfBirth: String?,
    @SerializedName("known_for_department")
    val knownForDepartment: String?,
    @SerializedName("imdb_id")
    val imdbId: String?,
)

fun CastDetailDto.toDomain(): CastUiModel? {
    val imageUrl = profilePath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: ""
    return CastUiModel(
        id = id,
        name = name,
        biography = biography.orEmpty(),
        birthday = birthday,
        profileImageUrl = imageUrl,
        placeOfBirth = placeOfBirth,
        knownForDepartment = knownForDepartment,
    )
}
