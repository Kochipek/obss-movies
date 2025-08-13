package com.ipekkochisarli.obssmovies.features.cast.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.features.cast.domain.CastRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CastViewModel
    @Inject
    constructor(
        private val repository: CastRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(CastDetailState())
        val uiState: StateFlow<CastDetailState> = _uiState.asStateFlow()

        fun loadCastDetail(castId: Int) {
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                when (val result = repository.getCastDetail(castId)) {
                    is ApiResult.Success -> _uiState.value = CastDetailState(castDetail = result.data)
                    is ApiResult.Error ->
                        _uiState.value =
                            CastDetailState(error = result.exception.message)
                }
            }
        }
    }
