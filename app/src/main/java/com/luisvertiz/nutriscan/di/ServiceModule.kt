package com.luisvertiz.nutriscan.di

import com.luisvertiz.nutriscan.features.foodcamera.ai.FirebaseAiService
import com.luisvertiz.nutriscan.features.foodcamera.ai.FirebaseAiServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    abstract fun bindFirebaseAIServiceImpl(
        firebaseAiServiceImpl: FirebaseAiServiceImpl
    ): FirebaseAiService
}
