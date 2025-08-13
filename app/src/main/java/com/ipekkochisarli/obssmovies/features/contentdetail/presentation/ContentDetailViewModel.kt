package com.ipekkochisarli.obssmovies.features.contentdetail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.features.contentdetail.DetailSectionType
import com.ipekkochisarli.obssmovies.features.contentdetail.data.dto.DetailSectionResult
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.ContentDetailUiModel
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.usecase.GetMovieDetailSectionUseCase
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.usecase.GetMovieDetailUseCase
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.FavoriteMovieUiModel
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.LibraryCategoryType
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
        private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ContentDetailUiState())
        val uiState: StateFlow<ContentDetailUiState> = _uiState

        fun loadMovieDetail(movieId: Int) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }

                when (
                    val detailResult =
                        getMovieDetailUseCase(
                            movieId,
                        )
                ) {
                    is ApiResult.Success -> {
                        _uiState.update { it.copy(detail = detailResult.data) }

                        loadFavoriteStatuses(movieId)

                        val sections =
                            listOf(
                                DetailSectionType.CREDITS,
                                DetailSectionType.VIDEOS,
                                DetailSectionType.SIMILAR_MOVIES,
                            )
                        sections.forEach { section ->
                            loadSection(movieId, section)
                        }
                    }

                    is ApiResult.Error -> {
                        _uiState.update { it.copy(message = detailResult.exception.message) }
                    }
                }
                _uiState.update { it.copy(isLoading = false) }
            }
        }

        private suspend fun loadSection(
            movieId: Int,
            section: DetailSectionType,
        ) {
            when (val result = getMovieDetailSectionUseCase(movieId, section)) {
                is ApiResult.Success -> {
                    when (val data = result.data) {
                        is DetailSectionResult.Credits -> _uiState.update { it.copy(credits = data.cast) }
                        is DetailSectionResult.Videos -> _uiState.update { it.copy(videos = data.videos) }
                        is DetailSectionResult.SimilarMovies -> _uiState.update { it.copy(similar = data.movies) }
                    }
                }

                is ApiResult.Error -> {
                    _uiState.update { it.copy(message = result.exception.message) }
                }
            }
        }

        private fun ContentDetailUiModel.toFavoriteMovieUiModel(listType: LibraryCategoryType) =
            FavoriteMovieUiModel(
                id = id,
                title = title,
                posterUrl = posterUrl,
                releaseYear = releaseYear.substringBefore("-"),
                listType = listType,
                description = overview,
            )

        fun loadFavoriteStatuses(movieId: Int) {
            viewModelScope.launch {
                val watchLaterResult = getFavoriteMoviesUseCase(LibraryCategoryType.WATCH_LATER)
                val watchedResult = getFavoriteMoviesUseCase(LibraryCategoryType.WATCHED)

                val isWatchLater =
                    (watchLaterResult as? ApiResult.Success)?.data?.any { it.id == movieId } ?: false
                val isWatched =
                    (watchedResult as? ApiResult.Success)?.data?.any { it.id == movieId } ?: false

                _uiState.update {
                    it.copy(
                        isAddedWatchLater = isWatchLater,
                        isWatched = isWatched,
                    )
                }
            }
        }

        fun toggleFavoriteStatus(listType: LibraryCategoryType) {
            val currentDetail = _uiState.value.detail ?: return
            viewModelScope.launch {
                val isCurrentlyFavorite =
                    when (listType) {
                        LibraryCategoryType.WATCH_LATER -> _uiState.value.isAddedWatchLater
                        LibraryCategoryType.WATCHED -> _uiState.value.isWatched
                    }

                if (isCurrentlyFavorite) {
                    removeFavoriteMovieUseCase(currentDetail.id, listType)
                } else {
                    addFavoriteMovieUseCase(currentDetail.toFavoriteMovieUiModel(listType), listType)
                }
                loadFavoriteStatuses(currentDetail.id)
            }
        }
    }
