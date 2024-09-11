package com.example.alarmpermissiontest.media3

import android.net.Uri
import androidx.media3.common.C

// 재생 상태를 관리하기 위한 PlaybackState
data class AudioQtState(
    val isPlaying: Boolean = false,
    val hasPrevious: Boolean = false,
    val hasNext: Boolean = false,
    val position: Long = C.TIME_UNSET,
    val duration: Long = C.TIME_UNSET,
    val speed: Float = 1F,
    val aspectRatio: Float = 16F / 9F,
    val title: String? = null,
    val artist: String? = null,
    val artworkUri: Uri? = null,
)