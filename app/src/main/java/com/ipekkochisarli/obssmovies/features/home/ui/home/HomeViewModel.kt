package com.ipekkochisarli.obssmovies.features.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.features.home.HomeSectionType
import com.ipekkochisarli.obssmovies.features.home.domain.GetMovieListBySectionUseCase
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getMovieListBySectionUseCase: GetMovieListBySectionUseCase,
    ) : ViewModel() {
        private val _uiStates = MutableStateFlow<List<HomeUiState>>(emptyList())
        val uiStates = _uiStates.asStateFlow()

        fun loadMovies() {
            viewModelScope.launch {
                val sections = HomeSectionType.entries.toTypedArray()
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
            }
        }

        private fun mapToHomeUiState(
            section: HomeSectionType,
            movies: List<MovieUiModel>,
        ): HomeUiState {
            val carouselImages =
                if (section == HomeSectionType.NOW_PLAYING) {
                    movies.take(3).mapNotNull { it.carouselUrl.takeIf { url -> url?.isNotBlank() ?: false } }
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
