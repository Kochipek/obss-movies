package com.ipekkochisarli.obssmovies.features.contentdetail.data.dto

import com.google.gson.annotations.SerializedName
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.VideoUiModel

data class VideosDto(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("results")
    val results: List<ResultsItem?>? = null,
)

data class ResultsItem(
    @SerializedName("site")
    val site: String? = null,
    @SerializedName("size")
    val size: Int? = null,
    @SerializedName("iso_3166_1")
    val iso31661: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("official")
    val official: Boolean? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("published_at")
    val publishedAt: String? = null,
    @SerializedName("iso_639_1")
    val iso6391: String? = null,
    @SerializedName("key")
    val key: String? = null,
)

fun VideosDto.toDomain(): List<VideoUiModel> =
    results
        .orEmpty()
        .filterNotNull()
        .filter { it.site == "YouTube" && !it.key.isNullOrBlank() }
        .map {
            VideoUiModel(
                name = it.name.orEmpty(),
                thumbnailUrl = "https://img.youtube.com/vi/${it.key}/0.jpg",
                videoUrl = "https://www.youtube.com/watch?v=${it.key}",
                id = it.id.orEmpty(),
            )
        }
