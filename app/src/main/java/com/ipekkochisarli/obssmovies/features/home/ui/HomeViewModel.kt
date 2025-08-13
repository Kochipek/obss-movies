package com.ipekkochisarli.obssmovies.features.home.ui

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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getMovieListBySectionUseCase: GetMovieListBySectionUseCase,
        private val preferencesManager: PreferencesManager,
        private val removeFavoriteMovieUseCase: RemoveFavoriteMovieUseCase,
        private val addFavoriteMovieUseCase: AddFavoriteMovieUseCase,
        private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    ) : ViewModel() {
        private val _uiStates = MutableStateFlow<List<HomeUiState>>(emptyList())
        val uiStates = _uiStates.asStateFlow()

        val isGuest = !preferencesManager.isUserLoggedIn()

        fun loadMovies() {
            viewModelScope.launch {
                val sections = HomeSectionType.entries.toList()
                val states =
                    sections.map { section ->
                        when (val result = getMovieListBySectionUseCase(section)) {
                            is ApiResult.Success -> mapToHomeUiState(section, result.data)
                            is ApiResult.Error ->
                                HomeUiState(
                                    type = section,
                                    title = section.name,
                                    movies = emptyList(),
                                    carouselImages = emptyList(),
                                )
                        }
                    }

                _uiStates.value = states

                states.forEach { state ->
                    state.movies.forEach { movie ->
                        loadFavoriteStatuses(movie.id)
                    }
                }
            }
        }

        fun toggleWatchlist(movieId: Int) {
            val movie = _uiStates.value.flatMap { it.movies }.find { it.id == movieId } ?: return
            viewModelScope.launch {
                val listType = LibraryCategoryType.WATCH_LATER
                val favoriteMovie = movie.toFavoriteMovieUiModel(listType)

                if (movie.isAddedWatchLater) {
                    removeFavoriteMovieUseCase(favoriteMovie.id, listType)
                } else {
                    addFavoriteMovieUseCase(favoriteMovie, listType)
                }

                loadFavoriteStatuses(movieId)
            }
        }

        fun loadFavoriteStatuses(movieId: Int) {
            viewModelScope.launch {
                val watchLaterResult = getFavoriteMoviesUseCase(LibraryCategoryType.WATCH_LATER)
                val isWatchLater =
                    (watchLaterResult as? ApiResult.Success)?.data?.any { it.id == movieId } ?: false

                _uiStates.update { states ->
                    states.map { state ->
                        state.copy(
                            movies =
                                state.movies.map { movie ->
                                    if (movie.id == movieId) {
                                        movie.copy(isAddedWatchLater = isWatchLater)
                                    } else {
                                        movie
                                    }
                                },
                        )
                    }
                }
            }
        }

        private var currentViewType = MovieViewType.LIST

        fun toggleViewType() {
            currentViewType =
                when (currentViewType) {
                    MovieViewType.LIST -> MovieViewType.GRID
                    MovieViewType.GRID -> MovieViewType.LIST
                    else -> MovieViewType.LIST
                }
            _uiStates.update { it.map { state -> state.copy(viewType = currentViewType) } }
        }

        private fun MovieUiModel.toFavoriteMovieUiModel(listType: LibraryCategoryType) =
            FavoriteMovieUiModel(
                id = id,
                title = title,
                posterUrl = posterUrl,
                releaseYear = releaseYear.substringBefore("-"),
                listType = listType,
                description = description.orEmpty(),
            )

        private fun mapToHomeUiState(
            section: HomeSectionType,
            movies: List<MovieUiModel>,
        ): HomeUiState {
            val carouselImages =
                if (section == HomeSectionType.NOW_PLAYING) {
                    movies.take(3).mapNotNull { it.carouselUrl?.takeIf { url -> url.isNotBlank() } }
                } else {
                    emptyList()
                }

            return HomeUiState(
                type = section,
                title = section.name,
                movies = movies,
                carouselImages = carouselImages,
            )
        }
    }
