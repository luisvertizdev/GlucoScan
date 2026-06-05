package com.luisvertiz.nutriscan.di

import com.luisvertiz.nutriscan.features.login.LoginRepository
import com.luisvertiz.nutriscan.features.nutritiongoal.NutritionGoalRepository
import com.luisvertiz.nutriscan.features.register.RegisterRepository
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
    fun provideRegisterRepository(): RegisterRepository {
        return RegisterRepository()
    }

    @Provides
    @Singleton
    fun provideLoginRepository(): LoginRepository {
        return LoginRepository()
    }

    @Provides
    @Singleton
    fun provideNutritionSetupRepository(): NutritionGoalRepository {
        return NutritionGoalRepository()
    }
}
