package com.example.alarmpermissiontest.media3

import android.content.Intent
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import javax.inject.Inject

class AudioQtService : MediaLibraryService() {

    lateinit var session : MediaLibrarySession

//    lateinit var scope : CoroutineScope

    lateinit var player : Player

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        if (!player.playWhenReady) {
            release()
            stopSelf()
        }
    }
//    player = Player()
    private fun release() {
        player.release()
//        session.release()
//        scope.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        release()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        return session.takeUnless { session ->
            session.invokeIsReleased
        }
        return null
    }

}


private val MediaSession.invokeIsReleased: Boolean
    get() = try {
        MediaSession::class.java.getDeclaredMethod("isReleased")
            .apply { isAccessible = true }
            .invoke(this) as Boolean
    } catch (e: Exception) {
        false
    }



//class PlaybackService : MediaSessionService() {
//
//    private var mediaSession: MediaSession? = null
//
//    // Create your player and media session in the onCreate lifecycle event
//    override fun onCreate() {
//        super.onCreate()
//        val player = ExoPlayer.Builder(this).build()
//        mediaSession = MediaSession.Builder(this, player).build()
//    }
//
//    // The user dismissed the app from the recent tasks
//    override fun onTaskRemoved(rootIntent: Intent?) {
//        val player = mediaSession?.player!!
//        if (!player.playWhenReady
//            || player.mediaItemCount == 0
//            || player.playbackState == Player.STATE_ENDED) {
//            // Stop the service if not playing, continue playing in the background
//            // otherwise.
//            stopSelf()
//        }
//    }
//
//    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
//        TODO("Not yet implemented")
//    }
//
//    // Remember to release the player and media session in onDestroy
//    override fun onDestroy() {
//        mediaSession?.run {
//            player.release()
//            release()
//            mediaSession = null
//        }
//        super.onDestroy()
//    }
//}
