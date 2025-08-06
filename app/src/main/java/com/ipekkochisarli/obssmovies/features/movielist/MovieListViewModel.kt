package com.ipekkochisarli.obssmovies.features.movielist

import androidx.lifecycle.ViewModel
import com.ipekkochisarli.obssmovies.common.MovieViewType
import com.ipekkochisarli.obssmovies.features.search.presentation.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow(SearchUiState(viewType = MovieViewType.LIST))
        val uiState = _uiState.asStateFlow()
        private var currentViewType = MovieViewType.LIST

        fun toggleViewType() {
            currentViewType =
                when (currentViewType) {
                    MovieViewType.LIST -> MovieViewType.GRID
                    MovieViewType.GRID -> MovieViewType.LIST
                    else -> MovieViewType.LIST
                }
            _uiState.value = _uiState.value.copy(viewType = currentViewType)
        }
    }
