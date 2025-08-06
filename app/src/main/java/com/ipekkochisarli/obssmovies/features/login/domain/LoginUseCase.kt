package com.ipekkochisarli.obssmovies.features.login.domain

import com.google.firebase.auth.FirebaseUser
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
        ): ApiResult<FirebaseUser> = authRepository.loginUser(email, password)
    }
