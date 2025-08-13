package com.ipekkochisarli.obssmovies.features.cast.presentation

import com.ipekkochisarli.obssmovies.features.cast.domain.CastUiModel

data class CastDetailState(
    val isLoading: Boolean = false,
    val castDetail: CastUiModel? = null,
    val error: String? = null,
)
