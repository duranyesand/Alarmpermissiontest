package com.example.alarmpermissiontest.media3.playback

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.session.MediaSession.MediaItemsWithStartPosition
import com.example.alarmpermissiontest.R
import com.example.alarmpermissiontest.media3.Room
import com.example.alarmpermissiontest.media3.Session
import javax.inject.Inject


class MediaItemProvider @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val application: Application,
) {

    fun root(): MediaItem = MediaItem(
        title = application.getString(R.string.media_session_root_title),
        description = application.getString(R.string.media_session_root_description),
        browsable = true,
        isPlayable = false,
        mediaId = MediaId.Root,
    )

    suspend fun item(id: String): MediaItem? {
        val mediaId = id.toMediaIdOrNull() ?: return null
        return when (mediaId) {
            MediaId.Root -> root()
            is MediaId.Session -> {
                sessionRepository.getSession(mediaId.id).let(::mediaItem)
            }

            is MediaId.Tag -> {
                mediaItem(mediaId)
            }

            is MediaId.Track -> {
                val sessions = sessionRepository.getSessions()
                mediaItem(mediaId, sessions)
            }
        }
    }

    suspend fun children(id: String): List<MediaItem>? {
        val mediaId = id.toMediaIdOrNull() ?: return null
        val sessions = runCatching { sessionRepository.getSessions() }.getOrNull() ?: return null
        return when (mediaId) {
            MediaId.Root -> {
                listOf(
                    mediaItem(track = MediaId.Track.Keynote, sessions = sessions),
                    mediaItem(track = MediaId.Track.TrackOne, sessions = sessions),
                    mediaItem(track = MediaId.Track.TrackTwo, sessions = sessions),
                    mediaItem(track = MediaId.Track.TrackThree, sessions = sessions),
                )
            }

            is MediaId.Track.Keynote -> {
                mediaItems(mediaId, sessions)
            }

            is MediaId.Track -> {
                mediaItems(mediaId, sessions)
            }

            is MediaId.Tag -> {
                mediaItems(mediaId, sessions)
            }

            is MediaId.Session -> {
                runCatching { sessionRepository.getSession(mediaId.id) }.getOrNull()
                    ?.let { session -> listOf(mediaItem(session)) }
            }
        }
    }

    private fun mediaItem(
        track: MediaId.Track,
        sessions: List<Session>,
    ) = MediaItem(
        title = when (track) {
            is MediaId.Track.Keynote -> "웅냥웅냥"
            is MediaId.Track.TrackOne -> application.getString(R.string.media_session_track_1_title)
            is MediaId.Track.TrackTwo -> application.getString(R.string.media_session_track_2_title)
            is MediaId.Track.TrackThree -> application.getString(R.string.media_session_track_3_title)
        },
        description = when (track) {
            is MediaId.Track.Keynote -> application.getString(R.string.media_session_keynote_description)
            is MediaId.Track.TrackOne -> application.getString(R.string.media_session_track_1_description)
            is MediaId.Track.TrackTwo -> application.getString(R.string.media_session_track_2_description)
            is MediaId.Track.TrackThree -> application.getString(R.string.media_session_track_3_description)
        },
        mediaId = track,
        browsable = true,
        isPlayable = false,
        imageUri = when (track) {
            is MediaId.Track.Keynote -> Uri.parse("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.hankyung.com%2Farticle%2F202112111705H&psig=AOvVaw1wudjdzzPuKdh05JgXW_JC&ust=1726757135742000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCNDx0ODdzIgDFQAAAAAdAAAAABAE")
            is MediaId.Track.TrackOne -> Uri.parse("https://raw.githubusercontent.com/workspace/media-samples/main/img/track1.jpg")
            is MediaId.Track.TrackTwo -> Uri.parse("https://raw.githubusercontent.com/workspace/media-samples/main/img/track2.jpg")
            is MediaId.Track.TrackThree -> Uri.parse("https://raw.githubusercontent.com/workspace/media-samples/main/img/track3.jpg")
        },
        artist = room(track).let { room ->
            sessions.filter { session -> session.room == room }
                .mapNotNull { it.speakers.firstOrNull() }
                .joinToString(", ") { it.name }
        },
    )

    private fun mediaItem(
        tag: MediaId.Tag
    ) = MediaItem(
        title = tag.name,
        description = "${tag.name}에 관한 발표 목록입니다.",
        mediaId = tag,
        browsable = true,
        isPlayable = false,
        imageUri = Uri.parse("https://raw.githubusercontent.com/workspace/media-samples/main/img/logo.jpg"),
    )

    private fun mediaItems(
        track: MediaId.Track.Keynote,
        sessions: List<Session>,
    ): List<MediaItem> {
        return room(track).let { room ->
            sessions.filter { session -> session.room == room }
                .map(::mediaItem)
        } + sessions
            .flatMap { session -> session.tags }
            .distinctBy { it.name }
            .map { tag -> MediaId.Tag(tag.name) }
            .map { tag -> mediaItem(tag) }
    }

    private fun mediaItems(
        track: MediaId.Track,
        sessions: List<Session>,
    ) = room(track).let { room ->
        sessions.filter { session -> session.room == room }
            .map(::mediaItem)
    }

    private fun mediaItems(
        tag: MediaId.Tag,
        sessions: List<Session>,
    ) = sessions
        .filter { session -> session.tags.any { it.name == tag.name } }
        .map(::mediaItem)

    @OptIn(UnstableApi::class)
    fun mediaItem(session: Session): MediaItem {
        // Log the manifestUrl for debugging purposes
        val manifestUrl = session.video?.manifestUrl
        Log.d("MediaItemProvider", "Manifest URL: $manifestUrl")

        return MediaItem(
            title = "하루 QT",
            description = session.content,
            mediaId = MediaId.Session(session.id),
            browsable = false,
            isPlayable = true,
//            sourceUri = manifestUrl?.let(Uri::parse),



//            sourceUri = Uri.parse("https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"),
            sourceUri = Uri.parse("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"),






//            sourceUri = Uri.parse("http://cloudvod1.cgntv.net/_NewMP4/1/MAQT240919.h640x360.mp4"),
            imageUri = Uri.parse("https://img.hankyung.com/photo/202112/03.28306045.1.jpg"),
            artist = session.speakers.joinToString(",") { it.name }
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

    private fun room(
        track: MediaId.Track
    ) = when (track) {
        is MediaId.Track.Keynote -> Room.ETC
        is MediaId.Track.TrackOne -> Room.TRACK1
        is MediaId.Track.TrackTwo -> Room.TRACK2
        is MediaId.Track.TrackThree -> Room.TRACK3
    }

}

