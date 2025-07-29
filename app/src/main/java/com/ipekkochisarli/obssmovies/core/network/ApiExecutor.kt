package com.ipekkochisarli.obssmovies.core.network

import retrofit2.HttpException
import retrofit2.Response

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ApiResult<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                ApiResult.Success(body)
            } else {
                ApiResult.Error("Response body is null.")
            }
        } else {
            ApiResult.Error(
                ApiError.handleException(
                    HttpException(response)
                )
            )
        }
    } catch (e: Exception) {
        ApiResult.Error(ApiError.handleException(e))
    }
}