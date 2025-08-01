package com.ipekkochisarli.obssmovies.features.home.di

import com.ipekkochisarli.obssmovies.features.home.data.MovieRepositoryImpl
import com.ipekkochisarli.obssmovies.features.home.data.remote.service.MovieApiService
import com.ipekkochisarli.obssmovies.features.home.domain.GetMovieListBySectionUseCase
import com.ipekkochisarli.obssmovies.features.home.domain.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class HomeModule {
    @Provides
    fun provideGuideRepository(movieApiService: MovieApiService): MovieRepository = MovieRepositoryImpl(movieApiService)

    @Provides
    fun provideMovieApiService(retrofit: Retrofit): MovieApiService = retrofit.create(MovieApiService::class.java)

    @Provides
    fun provideHomeSectionUseCase(movieRepository: MovieRepository): GetMovieListBySectionUseCase =
        GetMovieListBySectionUseCase(movieRepository)
}
