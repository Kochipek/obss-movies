package com.ipekkochisarli.obssmovies.features.movie.data.remote.service

import com.ipekkochisarli.obssmovies.BuildConfig.API_KEY
import com.ipekkochisarli.obssmovies.features.movie.data.remote.dto.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {

    @GET(POPULAR_MOVIES)
    suspend fun getPopularMovies(
        @Query("apiKey") apiKey: String = API_KEY,
        @Query("language") language: String = LANGUAGE,
        @Query("page") page: Int
    ): MovieResponse

    companion object {
        const val POPULAR_MOVIES = "movie/popular"
        const val LANGUAGE = "en-US"
    }
}