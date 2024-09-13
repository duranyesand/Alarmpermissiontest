package com.example.alarmpermissiontest.media3.domain.etc

import android.app.Service
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter
import androidx.media3.session.LibraryResult
import androidx.media3.session.LibraryResult.RESULT_ERROR_NOT_SUPPORTED
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaLibraryService.LibraryParams
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSession.ControllerInfo
import androidx.media3.session.SessionError

import com.example.alarmpermissiontest.media3.AudioQtService
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.guava.future
import javax.inject.Inject

@UnstableApi @Module
@InstallIn(ServiceComponent::class)
internal object PlaybackModule {

//    @Provides
//    @ServiceScoped
//    fun player(
//        service: Service,
//        playbackStateListener: PlaybackStateListener,
//    ): Player {
//        val dataSourceFactory = DefaultDataSource.Factory(service)
//        val mediaSourceFactory = DashMediaSource.Factory(
//            DefaultDashChunkSource.Factory(
//                dataSourceFactory.setTransferListener(
//                    DefaultBandwidthMeter.Builder(service).build()
//                )
//            ),
//            dataSourceFactory
//        )
//        val audioAttributes = AudioAttributes.Builder()
//            .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
//            .setUsage(C.USAGE_MEDIA)
//            .build()
//        val renderersFactory = DefaultRenderersFactory(service)
//            .forceEnableMediaCodecAsynchronousQueueing()
//        return ExoPlayer.Builder(service, renderersFactory, mediaSourceFactory)
//            .setAudioAttributes(audioAttributes, true)
//            .setHandleAudioBecomingNoisy(true)
//            .build()
//            .also { player ->
//                playbackStateListener.attachTo(player)
//            }
//    }
//
//    @Provides
//    @ServiceScoped
//    fun scope(): CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    @Provides
    @ServiceScoped
    fun session(
        service: Service,
        player: Player,
        callback: LibrarySessionCallback,
        sessionActivityIntentProvider: SessionActivityIntentProvider
    ): MediaLibraryService.MediaLibrarySession {
        return MediaLibraryService.MediaLibrarySession.Builder(service as AudioQtService, player, callback)
            .apply {
                val pendingIntent = sessionActivityIntentProvider.toPlayer()
                if (pendingIntent != null) {
                    setSessionActivity(pendingIntent)
                }
            }
            .build()
    }
}



@UnstableApi
internal class LibrarySessionCallback @Inject constructor(
    private val mediaItemProvider: MediaItemProvider,
    private val scope: CoroutineScope,
) : MediaLibrarySession.Callback {

    override fun onGetLibraryRoot(
        session: MediaLibrarySession,
        browser: ControllerInfo,
        params: LibraryParams?,
    ): ListenableFuture<LibraryResult<MediaItem>> {
        if (params?.isRecent == true) {
            return Futures.immediateFuture(LibraryResult.ofError(SessionError.ERROR_NOT_SUPPORTED))
        }
        return Futures.immediateFuture(LibraryResult.ofItem(mediaItemProvider.root(), params))
    }

    override fun onGetItem(
        session: MediaLibrarySession,
        browser: ControllerInfo,
        mediaId: String,
    ): ListenableFuture<LibraryResult<MediaItem>> = scope.future {
        val item = mediaItemProvider.item(mediaId)
        if (item != null) {
            LibraryResult.ofItem(item, null)
        } else {
            LibraryResult.ofError(SessionError.ERROR_BAD_VALUE)
        }
    }

    @OptIn(UnstableApi::class)
    override fun onGetChildren(
        session: MediaLibrarySession,
        browser: ControllerInfo,
        parentId: String,
        page: Int,
        pageSize: Int,
        params: LibraryParams?,
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> = scope.future {
        val children = mediaItemProvider.children(parentId)
        if (children != null) {
            LibraryResult.ofItemList(children, params)
        } else {
            LibraryResult.ofError(SessionError.ERROR_BAD_VALUE)
        }
    }

    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: ControllerInfo,
        mediaItems: List<MediaItem>
    ): ListenableFuture<List<MediaItem>> = scope.future {
        mediaItems.map { mediaItem ->
            mediaItemProvider.item(mediaItem.mediaId) ?: mediaItem
        }
    }

    @OptIn(UnstableApi::class)
    override fun onSubscribe(
        session: MediaLibrarySession,
        browser: ControllerInfo,
        parentId: String,
        params: LibraryParams?
    ): ListenableFuture<LibraryResult<Void>> = scope.future {
        val children = mediaItemProvider.children(parentId)
            ?: return@future LibraryResult.ofError(SessionError.ERROR_BAD_VALUE)
        session.notifyChildrenChanged(browser, parentId, children.size, params)
        LibraryResult.ofVoid()
    }

    override fun onPlaybackResumption(
        mediaSession: MediaSession,
        controller: ControllerInfo
    ): ListenableFuture<MediaSession.MediaItemsWithStartPosition> {
        return scope.future {
            mediaItemProvider.currentMediaItemsOrKeynote()

        }
    }
}
