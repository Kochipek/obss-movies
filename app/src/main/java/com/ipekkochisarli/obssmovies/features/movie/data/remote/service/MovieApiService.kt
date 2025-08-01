package com.ipekkochisarli.obssmovies.features.movie.data.remote.service

import com.ipekkochisarli.obssmovies.BuildConfig.API_KEY
import com.ipekkochisarli.obssmovies.features.movie.data.remote.dto.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface MovieApiService {
    @GET
    suspend fun getMovies(
        @Url endpoint: String,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = LANGUAGE,
        @Query("page") page: Int,
    ): Response<MovieResponse>

    companion object {
        const val LANGUAGE = "en-US"
    }
}
