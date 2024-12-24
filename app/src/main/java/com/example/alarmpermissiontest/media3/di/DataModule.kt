package com.example.alarmpermissiontest.media3.di

import android.content.Context
import com.example.alarmpermissiontest.media3.playback.AssetsGithubRawApi
import com.example.alarmpermissiontest.media3.playback.DefaultPlaybackPreferencesDataSource
import com.example.alarmpermissiontest.media3.playback.DefaultSessionPreferencesDataSource
import com.example.alarmpermissiontest.media3.playback.DefaultSessionRepository
//import com.example.alarmpermissiontest.media3.playback.DefaultSessionRepository
import com.example.alarmpermissiontest.media3.playback.GithubRawApi
import com.example.alarmpermissiontest.media3.playback.PlaybackPreferencesDataSource
import com.example.alarmpermissiontest.media3.playback.SessionPreferencesDataSource
import com.example.alarmpermissiontest.media3.playback.SessionRepository
//import com.example.alarmpermissiontest.media3.playback.SessionRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class DataModule {

    @Binds
    abstract fun bindPlaybackLocalDataSource(
        dataSource: DefaultPlaybackPreferencesDataSource,
    ): PlaybackPreferencesDataSource

    @Binds
    abstract fun bindSessionLocalDataSource(
        dataSource: DefaultSessionPreferencesDataSource,
    ): SessionPreferencesDataSource

    @InstallIn(SingletonComponent::class)
    @Module
    internal object FakeModule {


        @Provides
        @Singleton
        fun provideSessionRepository(
            githubRawApi: GithubRawApi,
            playbackPreferencesDataSource: PlaybackPreferencesDataSource,
            sessionDataSource: SessionPreferencesDataSource,
        ): SessionRepository =
            DefaultSessionRepository(
                githubRawApi,
                playbackPreferencesDataSource,
                sessionDataSource
            )

        @Provides
        @Singleton
        fun provideGithubRawApi(
            @ApplicationContext context: Context,
        ): AssetsGithubRawApi = AssetsGithubRawApi(context)
    }
}