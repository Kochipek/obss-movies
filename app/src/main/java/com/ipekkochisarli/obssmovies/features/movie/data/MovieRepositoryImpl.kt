package com.ipekkochisarli.obssmovies.features.movie.data

import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.core.network.safeApiCall
import com.ipekkochisarli.obssmovies.features.movie.HomeSectionType
import com.ipekkochisarli.obssmovies.features.movie.data.remote.dto.toDomain
import com.ipekkochisarli.obssmovies.features.movie.data.remote.service.MovieApiService
import com.ipekkochisarli.obssmovies.features.movie.domain.MovieRepository
import com.ipekkochisarli.obssmovies.features.movie.domain.MovieUiModel
import javax.inject.Inject

class MovieRepositoryImpl
    @Inject
    constructor(
        private val movieApiService: MovieApiService,
    ) : MovieRepository {
        override suspend fun getMovieListBySection(section: HomeSectionType): ApiResult<List<MovieUiModel>> {
            val apiResult =
                safeApiCall {
                    when (section) {
                        HomeSectionType.POPULAR -> movieApiService.getPopularMovies(page = 1)
//                HomeSectionType.NOW_PLAYING -> TODO()
//                HomeSectionType.TOP_RATED -> TODO()
//                HomeSectionType.UPCOMING -> TODO()
                    }
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
