package com.ipekkochisarli.obssmovies.features.login.domain

import com.google.firebase.auth.FirebaseUser
import com.ipekkochisarli.obssmovies.core.network.ApiResult
import javax.inject.Inject

class GoogleLoginUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        suspend operator fun invoke(idToken: String): ApiResult<FirebaseUser> = authRepository.loginWithGoogle(idToken)
    }
