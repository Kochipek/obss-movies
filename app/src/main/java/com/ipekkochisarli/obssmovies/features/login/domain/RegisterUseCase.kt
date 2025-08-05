package com.ipekkochisarli.obssmovies.features.login.domain

import com.ipekkochisarli.obssmovies.core.network.ApiResult

class RegisterUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
    ) {
        when (val result = authRepository.registerUser(email, password)) {
            is ApiResult.Success -> true
            is ApiResult.Error -> result.exception.message
        }
    }
}
