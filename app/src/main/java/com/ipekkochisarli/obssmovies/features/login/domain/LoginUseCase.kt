package com.ipekkochisarli.obssmovies.features.login.domain

import com.ipekkochisarli.obssmovies.core.network.ApiResult
import javax.inject.Inject

class LoginUserUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        suspend operator fun invoke(
            email: String,
            password: String,
        ) {
            when (val result = authRepository.loginUser(email, password)) {
                is ApiResult.Success -> true
                is ApiResult.Error -> result.exception.message
            }
        }
    }
