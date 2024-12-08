package com.example.alarmpermissiontest.media3.playback.di

import android.app.Service
import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.dash.DefaultDashChunkSource
import androidx.media3.exoplayer.hls.HlsDataSourceFactory
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.SessionCommand
import com.example.alarmpermissiontest.R
import com.example.alarmpermissiontest.media3.AudioQT.AudioQtStateListener
import com.example.alarmpermissiontest.media3.AudioQtService
import com.example.alarmpermissiontest.media3.playback.LibrarySessionCallback
import com.example.alarmpermissiontest.media3.playback.SessionActivityIntentProvider
import com.example.alarmpermissiontest.media3.viewmodel.PlayerViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path

@UnstableApi
@Module
@InstallIn(ServiceComponent::class)
object PlaybackModule {

    @Provides
    @ServiceScoped
    fun player(
        service: Service,
        playbackStateListener: AudioQtStateListener,
    ): Player {
        val dataSourceFactory = DefaultDataSource.Factory(service)
        val hlsDataSourceFactory = DefaultHttpDataSource.Factory()
//        val mediaSourceFactory = ProgressiveMediaSource.Factory(dataSourceFactory)

//        val mediaSourceFactory = DashMediaSource.Factory(
//            DefaultDashChunkSource.Factory(
//                dataSourceFactory.setTransferListener(
//                    DefaultBandwidthMeter.Builder(service).build()
//                )
//            ),
//            dataSourceFactory
//        )

        val mediaSourceFactory = HlsMediaSource.Factory(
            hlsDataSourceFactory

        )

        val audioAttributes = AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
            .setUsage(C.USAGE_MEDIA)
            .build()

        val renderersFactory = DefaultRenderersFactory(service)
            .forceEnableMediaCodecAsynchronousQueueing()

        return ExoPlayer.Builder(service, renderersFactory, mediaSourceFactory)
            .setAudioAttributes(audioAttributes, true)
            .setHandleAudioBecomingNoisy(true)
            .build()
            .also { player ->
                playbackStateListener.attachTo(player)
            }

    }

    @Provides
    @ServiceScoped
    fun scope(): CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    @Provides
    @ServiceScoped
    fun session(
        service: Service,
        player: Player,
        callback: LibrarySessionCallback,
        sessionActivityIntentProvider: SessionActivityIntentProvider,
//        savedStateHandle: SavedStateHandle
    ): MediaLibraryService.MediaLibrarySession {


        player.removeMediaItem(1)

//        val previousCommand = SessionCommand("previous", Bundle.EMPTY)
//        val prevButton =
//            CommandButton.Builder().setDisplayName("prev").setSessionCommand(previousCommand)
//                .setIconResId(R.drawable.ic_launcher_background).build()
//
//        val playCommand = SessionCommand("play", Bundle.EMPTY)
//        val playButton =
//            CommandButton.Builder().setDisplayName("play").setSessionCommand(playCommand)
//                .setIconResId(R.drawable.ic_launcher_background).build()
//
//        val nextCommand = SessionCommand("next", Bundle.EMPTY)
//        val nextButton =
//            CommandButton.Builder().setDisplayName("next").setSessionCommand(nextCommand)
//                .setIconResId(R.drawable.ic_launcher_background).build()


        return MediaLibraryService.MediaLibrarySession.Builder(
            service as AudioQtService,
            player,
            callback
        )
            .apply {
                val pendingIntent = sessionActivityIntentProvider.toPlayer()
                if (pendingIntent != null) {
                    setSessionActivity(pendingIntent)
                }
            }
            .setCustomLayout(
                mutableListOf(
//                    prevButton,
//                    playButton,
//                    nextButton
                )
            )
            .build()
    }

}

internal interface GithubApi {
    @GET("repos/{owner}/{name}/contributors")
    suspend fun getContributors(
        @Path("owner") owner: String,
        @Path("name") name: String,
    ): List<ContributorResponse>
}

@Serializable
internal data class ContributorResponse(
    @SerialName("login") val name: String,
    @SerialName("avatar_url") val imageUrl: String,
    @SerialName("html_url") val githubUrl: String,
)


