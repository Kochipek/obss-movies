package com.ipekkochisarli.obssmovies.features.home.data

import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.core.network.safeApiCall
import com.ipekkochisarli.obssmovies.features.contentdetail.DetailSectionType
import com.ipekkochisarli.obssmovies.features.contentdetail.data.dto.DetailSectionResult
import com.ipekkochisarli.obssmovies.features.contentdetail.data.dto.toDomain
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.ContentDetailUiModel
import com.ipekkochisarli.obssmovies.features.home.HomeSectionType
import com.ipekkochisarli.obssmovies.features.home.data.remote.dto.toDomain
import com.ipekkochisarli.obssmovies.features.home.data.remote.service.MovieApiService
import com.ipekkochisarli.obssmovies.features.home.domain.MovieRepository
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel
import javax.inject.Inject

class MovieRepositoryImpl
    @Inject
    constructor(
        private val movieApiService: MovieApiService,
    ) : MovieRepository {
        override suspend fun getMovieListBySection(section: HomeSectionType): ApiResult<List<MovieUiModel>> {
            val apiResult =
                safeApiCall {
                    movieApiService.getMovies(endpoint = section.endpoint, page = 1)
                }

            return when (apiResult) {
                is ApiResult.Success -> {
                    val movies =
                        apiResult.data.results
                            ?.mapNotNull { it?.toDomain() }
                            ?: emptyList()
                    ApiResult.Success(movies)
                }

                is ApiResult.Error -> ApiResult.Error(apiResult.exception)
            }
        }

        override suspend fun searchMovie(query: String): ApiResult<List<MovieUiModel>> {
            val apiResult =
                safeApiCall {
                    movieApiService.searchMovie(query = query, page = 1)
                }
            return when (apiResult) {
                is ApiResult.Success -> {
                    val movies =
                        apiResult.data.results
                            ?.mapNotNull { it?.toDomain() }
                            ?: emptyList()
                    ApiResult.Success(movies)
                }

                is ApiResult.Error -> ApiResult.Error(apiResult.exception)
            }
        }

        override suspend fun getMovieDetail(movieId: Int): ApiResult<ContentDetailUiModel> {
            val apiResult = safeApiCall { movieApiService.getMovieDetail(movieId) }
            return when (apiResult) {
                is ApiResult.Success -> ApiResult.Success(apiResult.data.toDomain())
                is ApiResult.Error -> ApiResult.Error(apiResult.exception)
            }
        }

        override suspend fun getMovieDetailSection(
            movieId: Int,
            section: DetailSectionType,
        ): ApiResult<DetailSectionResult> =
            when (section) {
                DetailSectionType.CREDITS -> {
                    val apiResult = safeApiCall { movieApiService.getCredits(movieId) }
                    when (apiResult) {
                        is ApiResult.Success -> {
                            val castList = apiResult.data.toDomain()
                            ApiResult.Success(DetailSectionResult.Credits(castList))
                        }

                        is ApiResult.Error -> ApiResult.Error(apiResult.exception)
                    }
                }

                DetailSectionType.VIDEOS -> {
                    val apiResult = safeApiCall { movieApiService.getVideos(movieId) }
                    when (apiResult) {
                        is ApiResult.Success -> {
                            val videosList = apiResult.data.toDomain()
                            ApiResult.Success(DetailSectionResult.Videos(videosList))
                        }

                        is ApiResult.Error -> ApiResult.Error(apiResult.exception)
                    }
                }

                DetailSectionType.SIMILAR_MOVIES -> {
                    val apiResult = safeApiCall { movieApiService.getSimilarMovies(movieId) }
                    when (apiResult) {
                        is ApiResult.Success -> {
                            val movies =
                                apiResult.data.results
                                    ?.mapNotNull { it?.toDomain() }
                                    .orEmpty()
                            ApiResult.Success(DetailSectionResult.SimilarMovies(movies))
                        }

                        is ApiResult.Error -> ApiResult.Error(apiResult.exception)
                    }
                }
            }
    }
