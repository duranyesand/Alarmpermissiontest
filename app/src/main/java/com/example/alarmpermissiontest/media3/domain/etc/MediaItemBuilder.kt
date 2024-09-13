package com.example.alarmpermissiontest.media3.domain.etc

import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes.AUDIO_MPEG
import com.example.alarmpermissiontest.R
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json


fun MediaItem(
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
//            .setMediaType(
//                when (mediaId) {
//                    is MediaId.Session -> MediaMetadata.MEDIA_TYPE_VIDEO
//                    is MediaId.Track -> MediaMetadata.MEDIA_TYPE_PLAYLIST
//                    is MediaId.Tag -> MediaMetadata.MEDIA_TYPE_PLAYLIST
//                    MediaId.Root -> MediaMetadata.MEDIA_TYPE_FOLDER_PLAYLISTS
//                },
//            )
            .build()

    return MediaItem.Builder()
        .setMediaId( "R.raw.psalm139" )
        .setMimeType(AUDIO_MPEG)
        .setUri((R.raw.psalm139).toString())
        .setMediaMetadata(metadata)
        .setUri(sourceUri)
        .build()

}

fun String.toMediaIdOrNull(): MediaId? =
    try {
        null
//        Json.decodeFromString(MediaId.serializer(), this)
    } catch (e: SerializationException) {
        null
    }
