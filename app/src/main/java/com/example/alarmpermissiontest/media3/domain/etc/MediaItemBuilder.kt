package com.example.alarmpermissiontest.media3.domain.etc

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.alarmpermissiontest.R


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
                    is MediaId.Session -> MediaMetadata.MEDIA_TYPE_VIDEO
                    is MediaId.Track -> MediaMetadata.MEDIA_TYPE_PLAYLIST
                    is MediaId.Tag -> MediaMetadata.MEDIA_TYPE_PLAYLIST
                    MediaId.Root -> MediaMetadata.MEDIA_TYPE_FOLDER_PLAYLISTS
                },
            )
            .build()

    return MediaItem.Builder()
        .setMediaId( "R.raw.psalm139" )
        .setMediaMetadata(metadata)
        .setUri(sourceUri)
        .build()
}

fun String.toMediaIdOrNull(): MediaId? =
    try {
        Json.decodeFromString(MediaId.serializer(), this)
    } catch (e: SerializationException) {
        null
    }
