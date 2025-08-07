package com.ipekkochisarli.obssmovies.features.contentdetail.data.dto

import com.ipekkochisarli.obssmovies.features.contentdetail.domain.CastUiModel
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.VideoUiModel
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel

sealed class DetailSectionResult {
    data class Credits(
        val cast: List<CastUiModel>,
    ) : DetailSectionResult()

    data class Videos(
        val videos: List<VideoUiModel>,
    ) : DetailSectionResult()

    data class SimilarMovies(
        val movies: List<MovieUiModel>,
    ) : DetailSectionResult()
}
