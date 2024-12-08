package com.example.alarmpermissiontest.media3.di

import android.content.Context
import androidx.compose.runtime.saveable.Saver
import androidx.lifecycle.SavedStateHandle
import com.example.alarmpermissiontest.media3.SessionActivityIntentProviderImpl
import com.example.alarmpermissiontest.media3.playback.SessionActivityIntentProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object AndroidModule {

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    fun toPlayerIntentProvider(
        impl: SessionActivityIntentProviderImpl
    ): SessionActivityIntentProvider = impl
}