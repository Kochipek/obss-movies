package com.ipekkochisarli.obssmovies.features.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.FavoriteMovieUiModel
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.LibraryCategoryType
import com.ipekkochisarli.obssmovies.features.favorites.domain.usecase.GetFavoriteMoviesUseCase
import com.ipekkochisarli.obssmovies.features.favorites.domain.usecase.RemoveFavoriteMovieUseCase
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteMoviesViewModel
    @Inject
    constructor(
        private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
        private val removeFavoriteMovieUseCase: RemoveFavoriteMovieUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<List<MovieUiModel>>(emptyList())
        val uiState: StateFlow<List<MovieUiModel>> = _uiState

        fun loadFavorites(listType: LibraryCategoryType) {
            viewModelScope.launch {
                val result = getFavoriteMoviesUseCase(listType)
                if (result is ApiResult.Success) {
                    _uiState.value = result.data.map { it.toMovieUiModel() }
                } else {
                    _uiState.value = emptyList()
                }
            }
        }

        fun removeFavoriteMovie(
            movieId: Int,
            listType: LibraryCategoryType,
        ) {
            viewModelScope.launch {
                removeFavoriteMovieUseCase(movieId, listType)
                loadFavorites(listType)
            }
        }

        private fun FavoriteMovieUiModel.toMovieUiModel(): MovieUiModel =
            MovieUiModel(
                id = id,
                title = title,
                posterUrl = posterUrl,
                releaseYear = releaseYear,
                description = description,
            )
    }
