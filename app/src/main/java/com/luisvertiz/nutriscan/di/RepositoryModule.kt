package com.luisvertiz.nutriscan.di

import com.luisvertiz.nutriscan.features.login.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLoginRepository(): LoginRepository {
        return LoginRepository()
    }
}
