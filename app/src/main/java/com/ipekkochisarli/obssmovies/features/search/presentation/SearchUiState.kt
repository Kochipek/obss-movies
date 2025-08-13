package com.ipekkochisarli.obssmovies.features.search.presentation

import androidx.paging.PagingData
import com.ipekkochisarli.obssmovies.common.MovieViewType
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel
import kotlinx.coroutines.flow.Flow

data class SearchUiState(
    val query: String = "",
    val viewType: MovieViewType = MovieViewType.LIST,
    val isLoading: Boolean = false,
    val error: String? = null,
    val results: Flow<PagingData<MovieUiModel>>? = null,
)
