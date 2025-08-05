package com.ipekkochisarli.obssmovies.core.network

import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

sealed class AppError(
    open val message: String,
) {
    // Auth
    sealed class AuthError(
        override val message: String,
    ) : AppError(message) {
        data class InvalidCredentials(
            override val message: String,
        ) : AuthError(message)

        data class UserNotFound(
            override val message: String,
        ) : AuthError(message)

        data class AccountDisabled(
            override val message: String,
        ) : AuthError(message)

        data class UnknownAuthError(
            override val message: String,
        ) : AuthError(message)
    }

    // Network
    sealed class NetworkError(
        override val message: String,
    ) : AppError(message) {
        object NoConnection : NetworkError("No internet connection.")

        data class HttpError(
            val code: Int,
            override val message: String,
        ) : NetworkError(message)

        object Timeout : NetworkError("Request timed out.")

        data class Unexpected(
            override val message: String,
        ) : NetworkError(message)
    }

    // Unknown
    data class Unknown(
        override val message: String,
    ) : AppError(message)

    companion object {
        fun fromException(exception: Throwable): AppError =
            when (exception) {
                is IOException -> NetworkError.NoConnection
                is HttpException ->
                    NetworkError.HttpError(
                        exception.code(),
                        "HTTP ${exception.code()} ${exception.message() ?: "error"}",
                    )

                is TimeoutException -> NetworkError.Timeout
                else -> Unknown(exception.localizedMessage ?: "Unexpected error occurred.")
            }
    }
}
