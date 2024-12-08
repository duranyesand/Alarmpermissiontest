package com.example.alarmpermissiontest.media3

import kotlinx.datetime.LocalDateTime

data class Session(
    val id: String,
    val title: String,
    val content: String,
    val speakers: List<Speaker>,
    val level: Level,
    val tags: List<Tag>,
    val room: Room,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val video: Video?,
    val isBookmarked: Boolean,
)

data class Speaker(
    val name: String,
    val introduction: String,
    val imageUrl: String,
)
enum class Level {
    ETC,
    BASIC,
    INTERMEDIATE,
    ADVANCED
}
@JvmInline
value class Tag(val name: String)
enum class Room {
    ETC,
    TRACK1,
    TRACK2,
    TRACK3
}
data class Video(
    val manifestUrl: String,
    val thumbnailUrl: String,
)