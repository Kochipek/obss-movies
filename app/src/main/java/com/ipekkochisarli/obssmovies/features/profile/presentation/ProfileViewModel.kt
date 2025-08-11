package com.ipekkochisarli.obssmovies.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipekkochisarli.obssmovies.core.data.PreferencesManager
import com.ipekkochisarli.obssmovies.features.profile.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
    @Inject
    constructor(
        private val preferencesManager: PreferencesManager,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ProfileUiState())
        val uiState = _uiState.asStateFlow()

        init {
            loadUserData()
        }

        private fun loadUserData() {
            viewModelScope.launch {
                _uiState.value =
                    ProfileUiState(
                        email = preferencesManager.getSavedEmail(),
                        username =
                            preferencesManager.getSavedUsername()
                                ?: preferencesManager.getSavedEmail()?.substringBefore("@"),
                    )
            }
        }

        fun logout() {
            preferencesManager.clearUserData()
            preferencesManager.setUserLoggedIn(false)
        }
    }
