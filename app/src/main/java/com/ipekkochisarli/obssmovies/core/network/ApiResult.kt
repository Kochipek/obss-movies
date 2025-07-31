package com.ipekkochisarli.obssmovies.core.network

sealed class ApiResult<out T> {
    data class Success<T>(
        val data: T,
    ) : ApiResult<T>()

    data class Error(
        val exception: String,
    ) : ApiResult<Nothing>()
}
