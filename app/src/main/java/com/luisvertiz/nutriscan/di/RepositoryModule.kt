package com.luisvertiz.nutriscan.di

import com.luisvertiz.nutriscan.features.foodcamera.FoodCameraRepository
import com.luisvertiz.nutriscan.features.foodcamera.ai.FirebaseAiService
import com.luisvertiz.nutriscan.features.foodresult.FoodResultRepository
import com.luisvertiz.nutriscan.features.home.HomeRepository
import com.luisvertiz.nutriscan.features.login.LoginRepository
import com.luisvertiz.nutriscan.features.nutritiongoal.NutritionGoalRepository
import com.luisvertiz.nutriscan.features.nutritionresult.NutritionResultRepository
import com.luisvertiz.nutriscan.features.profile.ProfileRepository
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
    fun provideNutritionGoalRepository(): NutritionGoalRepository {
        return NutritionGoalRepository()
    }

    @Provides
    @Singleton
    fun provideNutritionResultRepository(): NutritionResultRepository {
        return NutritionResultRepository()
    }

    @Provides
    @Singleton
    fun provideProfileRepository(): ProfileRepository {
        return ProfileRepository()
    }

    @Provides
    @Singleton
    fun provideHomeRepository(): HomeRepository {
        return HomeRepository()
    }

    @Provides
    @Singleton
    fun provideFoodCameraRepository(
        firebaseAiService: FirebaseAiService,
    ): FoodCameraRepository {
        return FoodCameraRepository(firebaseAiService)
    }

    @Provides
    @Singleton
    fun provideFoodResultRepository(): FoodResultRepository {
        return FoodResultRepository()
    }


}
