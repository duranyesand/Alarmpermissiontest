package com.example.alarmpermissiontest.media3.playback

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.session.MediaSession.MediaItemsWithStartPosition
import com.example.alarmpermissiontest.R
import com.example.alarmpermissiontest.media3.Room
import com.example.alarmpermissiontest.media3.Session
import javax.inject.Inject


class MediaItemProvider @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val application: Application,
) {

    @OptIn(UnstableApi::class)
    fun mediaItem(session: Session): MediaItem {
        return MediaItem(
            title = "하루 QT",
            description = session.content,
            mediaId = MediaId.Session(session.id),
            browsable = false,
            isPlayable = true,
//            sourceUri = Uri.parse("https://japaneast.av.mk.io/dupluscdn-duranno-mkio/e6c45cc9-c759-4545-8f25-935219a9dde3/2fa6e34d-e1c0-4d82-b599-a6268622.ism/audio300_196096.m3u8(encryption=cbc)"),
            sourceUri = Uri.parse("https://japaneast.av.mk.io/dupluscdn-duranno-mkio/9abe0639-41fe-4812-9243-b28f20577258/7b8de0b2-f8ca-4d4d-a106-23f87cce.ism/manifest(format=m3u8-cmaf)"),
            imageUri = Uri.parse("https://img.hankyung.com/photo/202112/03.28306045.1.jpg"),
        )
    }

    @OptIn(UnstableApi::class)
    suspend fun currentMediaItemsOrKeynote(): MediaItemsWithStartPosition {
        val currentPlayingSessionId = sessionRepository.getCurrentPlayingSession()?.id ?: "1"

        val session = sessionRepository.getSession(currentPlayingSessionId)
        return MediaItemsWithStartPosition(
            listOf(mediaItem(session)),
            C.INDEX_UNSET,
            C.TIME_UNSET,
        )
    }
}

