package com.ipekkochisarli.obssmovies.core.network

import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

object ApiError {

    fun handleException(exception: Throwable): String {
        return when (exception) {
            is IOException -> "Network error. Please check your connection."
            is HttpException -> "Server error: ${exception.code()}"
            is TimeoutException -> "Request timed out. Please try again."
            else -> "Unexpected error occurred."
        }
    }
}