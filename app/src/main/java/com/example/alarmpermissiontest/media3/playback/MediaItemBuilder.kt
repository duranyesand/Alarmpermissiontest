package com.example.alarmpermissiontest.media3.playback

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

internal fun MediaItem(
    title: String,
    mediaId: MediaId,
    isPlayable: Boolean,
    browsable: Boolean,
    description: String? = null,
    album: String? = null,
    artist: String? = null,
    genre: String? = null,
    sourceUri: Uri? = null,
    imageUri: Uri? = null,
): MediaItem {
    val metadata =
        MediaMetadata.Builder()
            .setAlbumTitle(album)
            .setTitle(title)
            .setDescription(description)
            .setArtist(artist)
            .setGenre(genre)
            .setIsBrowsable(browsable)
            .setIsPlayable(isPlayable)
            .setArtworkUri(imageUri)
            .setMediaType(
                when (mediaId) {
                    is MediaId.Session -> MediaMetadata.MEDIA_TYPE_AUDIO_BOOK
                    is MediaId.Track -> MediaMetadata.MEDIA_TYPE_PLAYLIST
                    is MediaId.Tag -> MediaMetadata.MEDIA_TYPE_PLAYLIST
                    MediaId.Root -> MediaMetadata.MEDIA_TYPE_FOLDER_PLAYLISTS
                },
            )
            .build()

    return MediaItem.Builder()
        .setMediaId(Json.encodeToString(MediaId.serializer(), mediaId))
        .setMediaMetadata(metadata)
        .setMimeType(MimeTypes.APPLICATION_M3U8)
        .setUri(sourceUri)
        .build()
}

// TODO()
//fun String.toMediaIdOrNull(): MediaId? =
//    try {
//        Json.decodeFromString(MediaId.serializer(), this)
//    } catch (e: SerializationException) {
//        null
//    }
