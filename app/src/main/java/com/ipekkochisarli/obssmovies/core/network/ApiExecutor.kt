package com.ipekkochisarli.obssmovies.core.network

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.TimeoutException

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ApiResult<T> =
    try {
        val response = apiCall()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                ApiResult.Success(body)
            } else {
                ApiResult.Error(AppError.Unknown("Response body is null."))
            }
        } else {
            ApiResult.Error(AppError.fromException(HttpException(response)))
        }
    } catch (e: Exception) {
        ApiResult.Error(AppError.fromException(e))
    }
