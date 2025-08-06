package com.ipekkochisarli.obssmovies.features.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipekkochisarli.obssmovies.common.MovieViewType
import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.features.home.HomeSectionType
import com.ipekkochisarli.obssmovies.features.home.domain.GetMovieListBySectionUseCase
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
        private val getMovieListBySectionUseCase: GetMovieListBySectionUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(SearchUiState())
        val uiState = _uiState.asStateFlow()

        init {
            loadPopularMovies()
        }

        fun loadPopularMovies() {
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                when (val result = getMovieListBySectionUseCase(HomeSectionType.POPULAR)) {
                    is ApiResult.Success -> {
                        _uiState.value =
                            _uiState.value.copy(
                                results = result.data,
                                isLoading = false,
                                error = null,
                                query = "",
                            )
                    }

                    is ApiResult.Error -> {
                        _uiState.value =
                            _uiState.value.copy(
                                error = result.exception.message,
                                isLoading = false,
                            )
                    }
                }
            }
        }

        fun search(query: String) {
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null, query = query)
                when (val result = getSearchUseCase.invoke(query)) {
                    is ApiResult.Success -> {
                        _uiState.value =
                            _uiState.value.copy(
                                results = result.data,
                                isLoading = false,
                                error = null,
                            )
                    }

                    is ApiResult.Error -> {
                        _uiState.value =
                            _uiState.value.copy(
                                error = result.exception.message,
                                isLoading = false,
                            )
                    }
                }
            }
        }

        fun clearSearchResults() {
            loadPopularMovies()
        }

        fun toggleViewType() {
            val newViewType =
                when (_uiState.value.viewType) {
                    MovieViewType.LIST -> MovieViewType.GRID
                    MovieViewType.GRID -> MovieViewType.LIST
                    else -> MovieViewType.LIST
                }
            _uiState.value = _uiState.value.copy(viewType = newViewType)
        }
    }
