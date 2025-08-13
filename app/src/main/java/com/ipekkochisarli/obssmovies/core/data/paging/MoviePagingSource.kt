package com.ipekkochisarli.obssmovies.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ipekkochisarli.obssmovies.features.home.data.remote.dto.toDomain
import com.ipekkochisarli.obssmovies.features.home.data.remote.service.MovieApiService
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel
import retrofit2.HttpException
import java.io.IOException

class MoviePagingSource(
    private val movieApiService: MovieApiService,
    private val sectionEndpoint: String,
    private val query: String? = null,
) : PagingSource<Int, MovieUiModel>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieUiModel> {
        val page = params.key ?: 1
        return try {
            val response =
                if (query != null) {
                    movieApiService.searchMovie(query = query, page = page)
                } else {
                    movieApiService.getMovies(endpoint = sectionEndpoint, page = page)
                }

            val movies = response.body()?.results?.mapNotNull { it?.toDomain() } ?: emptyList()
            val nextKey = if (movies.isEmpty()) null else page + 1

            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextKey,
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieUiModel>): Int? =
        state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
}
