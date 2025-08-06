package com.ipekkochisarli.obssmovies.features.login.presentation

data class LoginUiState(
    val isUserLoggedIn: Boolean = false,
    val isUserSignedUp: Boolean = false,
    val isEmailAlreadyExists: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
