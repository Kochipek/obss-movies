package com.ipekkochisarli.obssmovies.features.favorites.di

import android.content.Context
import androidx.room.Room
import com.ipekkochisarli.obssmovies.core.data.MovieDb
import com.ipekkochisarli.obssmovies.features.favorites.data.FavoriteRepositoryImpl
import com.ipekkochisarli.obssmovies.features.favorites.data.local.dao.FavoriteMovieDao
import com.ipekkochisarli.obssmovies.features.favorites.domain.FavoriteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FavoriteRepositoryModule {
    @Binds
    abstract fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository
}

@Module
@InstallIn(SingletonComponent::class)
object FavoriteDatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): MovieDb =
        Room
            .databaseBuilder(context, MovieDb::class.java, "movie_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideFavoriteMovieDao(db: MovieDb): FavoriteMovieDao = db.favoriteMovieDao()
}
