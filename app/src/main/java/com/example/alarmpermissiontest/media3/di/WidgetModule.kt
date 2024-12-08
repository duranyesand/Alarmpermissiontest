package com.example.alarmpermissiontest.media3.di

import com.example.alarmpermissiontest.media3.playback.GetSessionUseCase
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetModule {

    fun getSessionUseCase(): GetSessionUseCase
}