package com.ipekkochisarli.obssmovies.features.login.data

import com.google.firebase.auth.FirebaseUser
import com.ipekkochisarli.obssmovies.core.data.PreferencesManager
import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.features.login.data.firebase.AuthDataSource
import com.ipekkochisarli.obssmovies.features.login.domain.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl
    @Inject
    constructor(
        private val authenticationDataSource: AuthDataSource,
        private val preferencesManager: PreferencesManager,
    ) : AuthRepository {
        override suspend fun registerUser(
            email: String,
            password: String,
            userName: String,
        ): ApiResult<FirebaseUser> {
            val result = authenticationDataSource.registerUser(email, password, userName)
            if (result is ApiResult.Success) {
                preferencesManager.setUserLoggedIn(true)
                preferencesManager.saveEmail(email)
                preferencesManager.saveUsername(userName)
            }
            return result
        }

        override suspend fun loginUser(
            email: String,
            password: String,
        ): ApiResult<FirebaseUser> {
            val result = authenticationDataSource.loginUser(email, password)
            if (result is ApiResult.Success) {
                preferencesManager.setUserLoggedIn(true)
                preferencesManager.saveEmail(email)
            }
            return result
        }

        override suspend fun loginWithGoogle(idToken: String): ApiResult<FirebaseUser> {
            val result = authenticationDataSource.loginWithGoogle(idToken)
            if (result is ApiResult.Success) {
                preferencesManager.setUserLoggedIn(true)
                preferencesManager.saveEmail(result.data.email ?: "")
            }
            return result
        }

        override suspend fun checkIfEmailExists(email: String) = authenticationDataSource.checkIfEmailExists(email)

        override suspend fun getCurrentUser() = authenticationDataSource.getCurrentUser()

        override suspend fun signOut() = authenticationDataSource.signOut()

        override suspend fun isUserLoggedIn() = authenticationDataSource.isUserLoggedIn()

        override suspend fun getCurrentUserId(): String? =
            when (val result = authenticationDataSource.getCurrentUser()) {
                is ApiResult.Success -> result.data.uid
                else -> null
            }
    }
