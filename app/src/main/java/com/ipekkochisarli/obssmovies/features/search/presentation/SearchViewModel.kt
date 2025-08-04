package com.ipekkochisarli.obssmovies.features.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipekkochisarli.obssmovies.common.MovieViewType
import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.features.search.domain.GetSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
    @Inject
    constructor(
        private val getSearchUseCase: GetSearchUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(SearchUiState())
        val uiState = _uiState.asStateFlow()

        private var currentViewType = MovieViewType.LIST

        fun search(query: String) {
            viewModelScope.launch {
                _uiState.value =
                    SearchUiState(isLoading = true, viewType = currentViewType, query = query)
                when (val result = getSearchUseCase.invoke(query)) {
                    is ApiResult.Success -> {
                        _uiState.value =
                            SearchUiState(
                                results = result.data,
                                viewType = currentViewType,
                                query = query,
                            )
                    }

                    is ApiResult.Error -> {
                        _uiState.value =
                            SearchUiState(
                                error = result.exception,
                                viewType = currentViewType,
                                query = query,
                            )
                    }
                }
            }
        }

        fun clearSearchResults() {
            _uiState.value = SearchUiState(viewType = currentViewType, query = "")
        }

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
