package com.example.alarmpermissiontest.media3.domain.model

data class Session(
    val id: String,
    val title: String,
    val content: String,
//    val speakers: List<Speaker>,
//    val level: Level,
//    val tags: List<Tag>,
//    val room: Room,
//    val startTime: LocalDateTime,
//    val endTime: LocalDateTime,
    val video: Video?,
//    val isBookmarked: Boolean,
)

data class Video(
    val manifestUrl: String,
    val thumbnailUrl: String,
)
