package com.ipekkochisarli.obssmovies.features.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipekkochisarli.obssmovies.common.MovieViewType
import com.ipekkochisarli.obssmovies.core.data.PreferencesManager
import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.FavoriteMovieUiModel
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.LibraryCategoryType
import com.ipekkochisarli.obssmovies.features.favorites.domain.usecase.AddFavoriteMovieUseCase
import com.ipekkochisarli.obssmovies.features.favorites.domain.usecase.GetFavoriteMoviesUseCase
import com.ipekkochisarli.obssmovies.features.favorites.domain.usecase.RemoveFavoriteMovieUseCase
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel
    @Inject
    constructor(
        private val addFavoriteMovieUseCase: AddFavoriteMovieUseCase,
        private val removeFavoriteMovieUseCase: RemoveFavoriteMovieUseCase,
        private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
        private val preferencesManager: PreferencesManager,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(MovieListUiState(viewType = MovieViewType.LIST))
        val uiState = _uiState.asStateFlow()

        private var currentViewType = MovieViewType.LIST

        val isGuest = !preferencesManager.isUserLoggedIn()

        fun setMovieList(list: List<MovieUiModel>) =
            viewModelScope.launch {
                val watchLaterIds =
                    (getFavoriteMoviesUseCase(LibraryCategoryType.WATCH_LATER) as? ApiResult.Success)?.data?.map { it.id }
                        ?: emptyList()
                val updatedList = list.map { it.copy(isAddedWatchLater = it.id in watchLaterIds) }
                _uiState.update { it.copy(results = updatedList) }
            }

        fun toggleViewType() {
            currentViewType =
                when (currentViewType) {
                    MovieViewType.LIST -> MovieViewType.GRID
                    MovieViewType.GRID -> MovieViewType.LIST
                    else -> MovieViewType.LIST
                }
            _uiState.update { it.copy(viewType = currentViewType) }
        }

        fun toggleWatchlist(movieId: Int) =
            viewModelScope.launch {
                val movie = _uiState.value.results.find { it.id == movieId } ?: return@launch
                val listType = LibraryCategoryType.WATCH_LATER

                if (movie.isAddedWatchLater) {
                    removeFavoriteMovieUseCase(movieId, listType)
                } else {
                    addFavoriteMovieUseCase(movie.toFavoriteMovie(listType), listType)
                }

                updateMovieFavoriteStatus(movieId)
            }

        private suspend fun updateMovieFavoriteStatus(movieId: Int) {
            val isAdded = isMovieInWatchLater(movieId)
            _uiState.update { state ->
                state.copy(
                    results =
                        state.results.map { movie ->
                            if (movie.id == movieId) movie.copy(isAddedWatchLater = isAdded) else movie
                        },
                )
            }
        }

        private suspend fun isMovieInWatchLater(movieId: Int): Boolean {
            val result = getFavoriteMoviesUseCase(LibraryCategoryType.WATCH_LATER)
            return (result as? ApiResult.Success)?.data?.any { it.id == movieId } ?: false
        }

        private fun MovieUiModel.toFavoriteMovie(listType: LibraryCategoryType) =
            FavoriteMovieUiModel(
                id = id,
                title = title,
                posterUrl = posterUrl,
                releaseYear = releaseYear.substringBefore("-"),
                listType = listType,
                description = description.orEmpty(),
            )
    }
