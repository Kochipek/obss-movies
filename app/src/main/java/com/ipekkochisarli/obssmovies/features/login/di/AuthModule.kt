package com.ipekkochisarli.obssmovies.features.login.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.ipekkochisarli.obssmovies.features.login.data.firebase.AuthDataSource
import com.ipekkochisarli.obssmovies.features.login.data.firebase.AuthDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthInstance(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideAuthenticationDataSource(firebaseAuth: FirebaseAuth): AuthDataSource = AuthDataSourceImpl(firebaseAuth)
}
