package com.luisvertiz.nutriscan.di

import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.PublicPreviewAPI
import com.google.firebase.ai.type.generationConfig
import com.luisvertiz.nutriscan.util.AIConstants.GEMINI_MODEL
import com.luisvertiz.nutriscan.util.AIConstants.MIME_TYPE_APPLICATION_JSON
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AIModule {

    @OptIn(PublicPreviewAPI::class)
    @Provides
    @Singleton
    fun provideGenerativeModel(): GenerativeModel {
        return Firebase.ai.generativeModel(
            modelName = GEMINI_MODEL,
            generationConfig = generationConfig {
                responseMimeType = MIME_TYPE_APPLICATION_JSON
            }
        )
    }
}
