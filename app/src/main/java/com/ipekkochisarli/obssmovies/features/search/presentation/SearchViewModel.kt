package com.ipekkochisarli.obssmovies.features.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ipekkochisarli.obssmovies.common.MovieViewType
import com.ipekkochisarli.obssmovies.features.home.HomeSectionType
import com.ipekkochisarli.obssmovies.features.home.domain.GetMovieListBySectionUseCase
import com.ipekkochisarli.obssmovies.features.search.domain.GetSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
    @Inject
    constructor(
        private val getSearchUseCase: GetSearchUseCase,
        private val getMovieListBySectionUseCase: GetMovieListBySectionUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(SearchUiState())
        val uiState = _uiState.asStateFlow()

        private val currentQuery = MutableStateFlow("")

        init {
            currentQuery
                .debounce(500)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    setLoading(true)
                    if (query.isBlank()) {
                        getMovieListBySectionUseCase(HomeSectionType.POPULAR)
                    } else {
                        getSearchUseCase(query)
                    }
                }.cachedIn(viewModelScope)
                .onEach { pagingData ->
                    _uiState.update {
                        it.copy(results = flowOf(pagingData), isLoading = false)
                    }
                }.launchIn(viewModelScope)
        }

        fun setQuery(query: String) {
            currentQuery.value = query
            _uiState.update { it.copy(query = query) }
        }

        fun toggleViewType() {
            val newType =
                when (_uiState.value.viewType) {
                    MovieViewType.LIST -> MovieViewType.GRID
                    MovieViewType.GRID -> MovieViewType.LIST
                    else -> MovieViewType.LIST
                }
            _uiState.update { it.copy(viewType = newType) }
        }

        private fun setLoading(isLoading: Boolean) {
            _uiState.update { it.copy(isLoading = isLoading) }
        }
    }
