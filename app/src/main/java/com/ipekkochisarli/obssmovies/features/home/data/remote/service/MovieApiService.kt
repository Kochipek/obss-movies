package com.ipekkochisarli.obssmovies.features.home.data.remote.service

import com.ipekkochisarli.obssmovies.BuildConfig.API_KEY
import com.ipekkochisarli.obssmovies.features.cast.data.CastDetailDto
import com.ipekkochisarli.obssmovies.features.contentdetail.data.dto.CreditsDto
import com.ipekkochisarli.obssmovies.features.contentdetail.data.dto.MovieDetailDto
import com.ipekkochisarli.obssmovies.features.contentdetail.data.dto.VideosDto
import com.ipekkochisarli.obssmovies.features.home.data.remote.dto.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET(SEARCH_MOVIE)
    suspend fun searchMovie(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = LANGUAGE,
        @Query("query") query: String,
        @Query("page") page: Int,
    ): Response<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY,
    ): Response<MovieDetailDto>

    @GET("movie/{movie_id}/credits")
    suspend fun getCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY,
    ): Response<CreditsDto>

    @GET("movie/{movie_id}/videos")
    suspend fun getVideos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY,
    ): Response<VideosDto>

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY,
    ): Response<MovieResponse>

    @GET("person/{person_id}")
    suspend fun getCastDetail(
        @Path("person_id") castId: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "en-US",
    ): CastDetailDto

    companion object {
        const val LANGUAGE = "en-US"
        const val SEARCH_MOVIE = "search/movie"
    }
}
