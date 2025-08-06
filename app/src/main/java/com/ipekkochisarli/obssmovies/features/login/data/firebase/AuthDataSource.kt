package com.ipekkochisarli.obssmovies.features.login.data.firebase

import com.google.firebase.auth.FirebaseUser
import com.ipekkochisarli.obssmovies.core.network.ApiResult

interface AuthDataSource {
    suspend fun registerUser(
        email: String,
        password: String,
        userName: String,
    ): ApiResult<FirebaseUser>

    suspend fun loginUser(
        email: String,
        password: String,
    ): ApiResult<FirebaseUser>

    suspend fun loginWithGoogle(idToken: String): ApiResult<FirebaseUser>

    suspend fun checkIfEmailExists(email: String): ApiResult<Boolean>

    suspend fun getCurrentUser(): ApiResult<FirebaseUser>

    suspend fun signOut(): ApiResult<Unit>

    suspend fun isUserLoggedIn(): ApiResult<Boolean>
}
