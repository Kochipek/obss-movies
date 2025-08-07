package com.ipekkochisarli.obssmovies.features.contentdetail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.features.contentdetail.DetailSectionType
import com.ipekkochisarli.obssmovies.features.contentdetail.data.dto.DetailSectionResult
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.usecase.GetMovieDetailSectionUseCase
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.usecase.GetMovieDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentDetailViewModel
    @Inject
    constructor(
        private val getMovieDetailUseCase: GetMovieDetailUseCase,
        private val getMovieDetailSectionUseCase: GetMovieDetailSectionUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ContentDetailUiState())
        val uiState: StateFlow<ContentDetailUiState> = _uiState

        fun loadMovieDetail(movieId: Int) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }

                when (val detailResult = getMovieDetailUseCase(movieId)) {
                    is ApiResult.Success -> {
                        _uiState.update { it.copy(detail = detailResult.data) }
                    }

                    is ApiResult.Error -> {
                        _uiState.update { it.copy(message = detailResult.exception.message) }
                    }
                }
                _uiState.update { it.copy(isLoading = false) }

                val sections =
                    listOf(
                        DetailSectionType.CREDITS,
                        DetailSectionType.VIDEOS,
                        DetailSectionType.SIMILAR_MOVIES,
                    )
                sections.forEach { section ->
                    launch { loadSection(movieId, section) }
                }
            }
        }

        private suspend fun loadSection(
            movieId: Int,
            section: DetailSectionType,
        ) {
            when (val result = getMovieDetailSectionUseCase(movieId, section)) {
                is ApiResult.Success -> {
                    when (val data = result.data) {
                        is DetailSectionResult.Credits -> {
                            _uiState.update { it.copy(credits = data.cast) }
                        }

                        is DetailSectionResult.Videos -> {
                            _uiState.update { it.copy(videos = data.videos) }
                        }

                        is DetailSectionResult.SimilarMovies -> {
                            _uiState.update { it.copy(similar = data.movies) }
                        }
                    }
                }

                is ApiResult.Error -> {
                    _uiState.update { it.copy(message = result.exception.message) }
                }
            }
        }
    }
