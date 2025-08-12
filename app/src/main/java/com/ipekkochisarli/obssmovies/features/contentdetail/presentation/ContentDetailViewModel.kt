package com.ipekkochisarli.obssmovies.features.contentdetail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.features.contentdetail.DetailSectionType
import com.ipekkochisarli.obssmovies.features.contentdetail.data.dto.DetailSectionResult
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.ContentDetailUiModel
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.usecase.GetMovieDetailSectionUseCase
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.usecase.GetMovieDetailUseCase
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.FavoriteListType
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.FavoriteMovieUiModel
import com.ipekkochisarli.obssmovies.features.favorites.domain.usecase.AddFavoriteMovieUseCase
import com.ipekkochisarli.obssmovies.features.favorites.domain.usecase.GetFavoriteMoviesUseCase
import com.ipekkochisarli.obssmovies.features.favorites.domain.usecase.RemoveFavoriteMovieUseCase
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
        private val addFavoriteMovieUseCase: AddFavoriteMovieUseCase,
        private val removeFavoriteMovieUseCase: RemoveFavoriteMovieUseCase,
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

        fun toggleFavorite(listType: FavoriteListType) {
            val currentDetail = _uiState.value.detail ?: return
            viewModelScope.launch {
                when (listType) {
                    FavoriteListType.WATCHED -> {
                        if (_uiState.value.isWatched) {
                            removeFavoriteMovieUseCase(currentDetail.id, listType)
                            _uiState.update { it.copy(isWatched = false) }
                        } else {
                            addFavoriteMovieUseCase(
                                currentDetail.toFavoriteMovieUiModel(listType),
                                listType,
                            )
                            _uiState.update { it.copy(isWatched = true) }
                        }
                    }

                    FavoriteListType.WATCH_LATER -> {
                        if (_uiState.value.isAddedWatchLater) {
                            removeFavoriteMovieUseCase(currentDetail.id, listType)
                            _uiState.update { it.copy(isAddedWatchLater = false) }
                        } else {
                            addFavoriteMovieUseCase(
                                currentDetail.toFavoriteMovieUiModel(listType),
                                listType,
                            )
                            _uiState.update { it.copy(isAddedWatchLater = true) }
                        }
                    }
                }
            }
        }

        private fun ContentDetailUiModel.toFavoriteMovieUiModel(listType: FavoriteListType) =
            FavoriteMovieUiModel(
                id = id,
                title = title,
                posterUrl = posterUrl,
                releaseYear = releaseYear.substringBefore("-"),
                listType = listType,
            )
    }
