package com.ipekkochisarli.obssmovies.features.login.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.ipekkochisarli.obssmovies.features.login.data.AuthRepositoryImpl
import com.ipekkochisarli.obssmovies.features.login.data.firebase.AuthDataSource
import com.ipekkochisarli.obssmovies.features.login.data.firebase.AuthDataSourceImpl
import com.ipekkochisarli.obssmovies.features.login.domain.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthInstance(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideAuthenticationDataSource(firebaseAuth: FirebaseAuth): AuthDataSource = AuthDataSourceImpl(firebaseAuth)

    @Provides
    @Singleton
    fun provideAuthRepository(authDataSource: AuthDataSource): AuthRepository = AuthRepositoryImpl(authDataSource)
}
