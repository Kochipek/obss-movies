package com.ipekkochisarli.obssmovies.features.login.data.firebase

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.*
import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.core.network.AppError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthDataSourceImpl
    @Inject
    constructor(
        private val firebaseAuth: FirebaseAuth,
    ) : AuthDataSource {
        override suspend fun registerUser(
            email: String,
            password: String,
            userName: String,
        ): ApiResult<FirebaseUser> =
            safeCall {
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                result.user ?: throw FirebaseAuthException("auth/null-user", "User registration failed")
            }

        override suspend fun loginUser(
            email: String,
            password: String,
        ): ApiResult<FirebaseUser> =
            safeCall {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                result.user ?: throw FirebaseAuthException("auth/null-user", "Login failed")
            }

        override suspend fun checkIfEmailExists(email: String): ApiResult<Boolean> =
            safeCall {
                val task: Task<SignInMethodQueryResult> = firebaseAuth.fetchSignInMethodsForEmail(email)
                val result: SignInMethodQueryResult = Tasks.await(task)
                !result.signInMethods.isNullOrEmpty()
            }

        override suspend fun getCurrentUser(): ApiResult<FirebaseUser> =
            firebaseAuth.currentUser?.let {
                ApiResult.Success(it)
            } ?: ApiResult.Error(AppError.AuthError.UserNotFound)

        override suspend fun signOut(): ApiResult<Unit> =
            safeCall {
                firebaseAuth.signOut()
            }

        override suspend fun isUserLoggedIn(): ApiResult<Boolean> =
            safeCall {
                firebaseAuth.currentUser != null
            }

        override suspend fun getCurrentUserId(): String? = firebaseAuth.currentUser?.uid

        override suspend fun loginWithGoogle(idToken: String): ApiResult<FirebaseUser> =
            safeCall {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = firebaseAuth.signInWithCredential(credential).await()
                authResult.user ?: throw FirebaseAuthException(
                    "auth/null-user",
                    "Google sign-in failed",
                )
            }

        private suspend fun <T> safeCall(block: suspend () -> T): ApiResult<T> =
            try {
                withContext(Dispatchers.IO) {
                    ApiResult.Success(block())
                }
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                ApiResult.Error(AppError.AuthError.InvalidCredentials("Invalid credentials"))
            } catch (e: FirebaseAuthInvalidUserException) {
                ApiResult.Error(AppError.AuthError.UserNotFound)
            } catch (e: FirebaseAuthException) {
                ApiResult.Error(AppError.AuthError.UnknownAuthError(e.localizedMessage ?: "Auth error"))
            } catch (e: Exception) {
                ApiResult.Error(AppError.Unknown(e.localizedMessage ?: "Unexpected error occurred"))
            }
    }
