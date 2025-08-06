package com.ipekkochisarli.obssmovies.features.login.data

import com.google.firebase.auth.FirebaseUser
import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.features.login.data.firebase.AuthDataSource
import com.ipekkochisarli.obssmovies.features.login.domain.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl
    @Inject
    constructor(
        private val authenticationDataSource: AuthDataSource,
    ) : AuthRepository {
        override suspend fun registerUser(
            email: String,
            password: String,
            userName: String,
        ): ApiResult<FirebaseUser> = authenticationDataSource.registerUser(email, password, userName)

        override suspend fun loginUser(
            email: String,
            password: String,
        ): ApiResult<FirebaseUser> = authenticationDataSource.loginUser(email, password)

        override suspend fun checkIfEmailExists(email: String): ApiResult<Boolean> = authenticationDataSource.checkIfEmailExists(email)
    }
