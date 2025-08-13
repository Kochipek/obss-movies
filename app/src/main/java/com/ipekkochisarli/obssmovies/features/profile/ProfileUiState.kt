package com.ipekkochisarli.obssmovies.features.profile

data class ProfileUiState(
    val email: String? = null,
    val username: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
