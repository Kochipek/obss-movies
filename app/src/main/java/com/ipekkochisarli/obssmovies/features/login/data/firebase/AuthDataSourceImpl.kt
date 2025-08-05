package com.ipekkochisarli.obssmovies.features.login.data.firebase

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.*
import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.core.network.AppError
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthDataSourceImpl
    @Inject
    constructor(
        private val firebaseAuth: FirebaseAuth,
    ) : AuthDataSource {
        override suspend fun registerUser(
            email: String,
            password: String,
        ): ApiResult<FirebaseUser> =
            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                result.user?.let {
                    ApiResult.Success(it)
                } ?: ApiResult.Error(AppError.AuthError.UnknownAuthError("User registration failed"))
            } catch (e: FirebaseAuthException) {
                ApiResult.Error(
                    AppError.AuthError.UnknownAuthError(
                        e.localizedMessage ?: "Registration failed",
                    ),
                )
            } catch (e: Exception) {
                ApiResult.Error(
                    AppError.AuthError.UnknownAuthError(
                        e.localizedMessage ?: "Unknown error",
                    ),
                )
            }

        override suspend fun loginUser(
            email: String,
            password: String,
        ): ApiResult<FirebaseUser> =
            try {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                result.user?.let {
                    ApiResult.Success(it)
                } ?: ApiResult.Error(AppError.AuthError.UnknownAuthError("Login failed"))
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                ApiResult.Error(AppError.AuthError.InvalidCredentials("Invalid credentials"))
            } catch (e: FirebaseAuthInvalidUserException) {
                ApiResult.Error(AppError.AuthError.UserNotFound("User not found"))
            } catch (e: FirebaseAuthException) {
                ApiResult.Error(
                    AppError.AuthError.UnknownAuthError(
                        e.localizedMessage ?: "Login failed",
                    ),
                )
            } catch (e: Exception) {
                ApiResult.Error(
                    AppError.AuthError.UnknownAuthError(
                        e.localizedMessage ?: "Unknown error",
                    ),
                )
            }

        override suspend fun checkIfEmailExists(email: String): ApiResult<Boolean> =
            try {
                val task: Task<SignInMethodQueryResult> = firebaseAuth.fetchSignInMethodsForEmail(email)
                val result: SignInMethodQueryResult = Tasks.await(task)
                val signInMethods = result.signInMethods
                ApiResult.Success(!signInMethods.isNullOrEmpty())
            } catch (e: FirebaseAuthException) {
                ApiResult.Error(
                    AppError.AuthError.UnknownAuthError(
                        e.localizedMessage ?: "Error checking email",
                    ),
                )
            } catch (e: Exception) {
                ApiResult.Error(
                    AppError.AuthError.UnknownAuthError(
                        e.localizedMessage ?: "Unknown error",
                    ),
                )
            }

        override suspend fun getCurrentUser(): ApiResult<FirebaseUser> {
            val currentUser = firebaseAuth.currentUser
            return if (currentUser != null) {
                ApiResult.Success(currentUser)
            } else {
                ApiResult.Error(AppError.AuthError.UserNotFound("User is not logged in"))
            }
        }

        override suspend fun signOut(): ApiResult<Unit> =
            try {
                firebaseAuth.signOut()
                ApiResult.Success(Unit)
            } catch (e: Exception) {
                ApiResult.Error(AppError.fromException(e))
            }

        override suspend fun isUserLoggedIn(): ApiResult<Boolean> =
            try {
                ApiResult.Success(firebaseAuth.currentUser != null)
            } catch (e: Exception) {
                ApiResult.Error(AppError.fromException(e))
            }
    }
