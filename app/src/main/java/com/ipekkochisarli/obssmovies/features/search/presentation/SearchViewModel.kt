package com.ipekkochisarli.obssmovies.features.search.presentation

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
import com.ipekkochisarli.obssmovies.features.home.HomeSectionType
import com.ipekkochisarli.obssmovies.features.home.domain.GetMovieListBySectionUseCase
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel
import com.ipekkochisarli.obssmovies.features.search.domain.GetSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
    @Inject
    constructor(
        private val getSearchUseCase: GetSearchUseCase,
        private val getMovieListBySectionUseCase: GetMovieListBySectionUseCase,
        private val addFavoriteMovieUseCase: AddFavoriteMovieUseCase,
        private val removeFavoriteMovieUseCase: RemoveFavoriteMovieUseCase,
        private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
        private val preferencesManager: PreferencesManager,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(SearchUiState())
        val uiState = _uiState.asStateFlow()

        private var searchJob: Job? = null

        val isGuest = !preferencesManager.isUserLoggedIn()

        init {
            loadPopularMovies()
        }

        fun loadPopularMovies() =
            viewModelScope.launch {
                setLoadingState(query = "")
                val result = getMovieListBySectionUseCase(HomeSectionType.POPULAR)
                handleMovieListResult(result)
            }

        fun search(query: String) {
            searchJob?.cancel()
            searchJob =
                viewModelScope.launch {
                    delay(500)
                    if (query.isBlank()) {
                        loadPopularMovies()
                        return@launch
                    }
                    setLoadingState(query)
                    val result = getSearchUseCase(query)
                    handleMovieListResult(result)
                }
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

        private fun setLoadingState(query: String) {
            _uiState.update { it.copy(isLoading = true, error = null, query = query) }
        }

        private suspend fun handleMovieListResult(result: ApiResult<List<MovieUiModel>>) {
            when (result) {
                is ApiResult.Success -> {
                    val movies =
                        result.data.map { it.copy(isAddedWatchLater = isMovieInWatchLater(it.id)) }
                    _uiState.update { it.copy(results = movies, isLoading = false) }
                }

                is ApiResult.Error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message,
                        )
                    }
            }
        }
    }
