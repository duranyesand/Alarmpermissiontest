package com.example.alarmpermissiontest.media3.di

import androidx.lifecycle.SavedStateHandle
import com.example.alarmpermissiontest.media3.AudioQT.AudioQtStateManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AudioQtStateModule {
    @Provides
    @Singleton
    fun provideAudioQtStateManager(): AudioQtStateManager {
        return AudioQtStateManager()
    }
}
