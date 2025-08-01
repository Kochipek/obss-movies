package com.ipekkochisarli.obssmovies.features.home.data

import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.core.network.safeApiCall
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
    }
