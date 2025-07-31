package com.ipekkochisarli.obssmovies.features.movie.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.features.movie.HomeSectionType
import com.ipekkochisarli.obssmovies.features.movie.domain.GetMovieListBySectionUseCase
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
                            is ApiResult.Success ->
                                HomeUiState(
                                    type = section,
                                    title = section.name,
                                    movies = result.data,
                                )

                            is ApiResult.Error ->
                                HomeUiState(
                                    type = section,
                                    title = section.name,
                                    movies = emptyList(),
                                )
                        }
                    }
                _uiStates.value = states
            }
        }
    }
