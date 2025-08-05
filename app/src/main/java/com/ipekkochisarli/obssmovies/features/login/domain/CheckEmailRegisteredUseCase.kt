package com.ipekkochisarli.obssmovies.features.login.domain

import com.ipekkochisarli.obssmovies.core.network.ApiResult
import javax.inject.Inject

class CheckEmailRegisteredUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        suspend operator fun invoke(email: String): Boolean =
            when (val result = authRepository.checkIfEmailExists(email)) {
                is ApiResult.Success -> result.data
                is ApiResult.Error -> false // ! todo handle error properly later
            }
    }
