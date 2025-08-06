package com.ipekkochisarli.obssmovies.features.login.domain

import com.google.firebase.auth.FirebaseUser
import com.ipekkochisarli.obssmovies.core.network.ApiResult

interface AuthRepository {
    suspend fun registerUser(
        email: String,
        password: String,
        userName: String,
    ): ApiResult<FirebaseUser>

    suspend fun loginUser(
        email: String,
        password: String,
    ): ApiResult<FirebaseUser>

    suspend fun checkIfEmailExists(email: String): ApiResult<Boolean>
}
