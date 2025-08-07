package com.ipekkochisarli.obssmovies.features.contentdetail.presentation

import com.ipekkochisarli.obssmovies.features.contentdetail.domain.CastUiModel
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.ContentDetailUiModel
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.VideoUiModel
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel

data class ContentDetailUiState(
    val detail: ContentDetailUiModel? = null,
    val credits: List<CastUiModel> = emptyList(),
    val videos: List<VideoUiModel> = emptyList(),
    val similar: List<MovieUiModel> = emptyList(),
    val message: String = "",
    val isLoading: Boolean = false,
)
