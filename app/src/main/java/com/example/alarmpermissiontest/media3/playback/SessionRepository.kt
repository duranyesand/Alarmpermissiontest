package com.example.alarmpermissiontest.media3.playback

import android.content.Context
import android.util.Log
import com.example.alarmpermissiontest.media3.Level
import com.example.alarmpermissiontest.media3.Room
import com.example.alarmpermissiontest.media3.Session
//import com.example.alarmpermissiontest.media3.Speaker
import com.example.alarmpermissiontest.media3.Tag
import com.example.alarmpermissiontest.media3.Video
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.datetime.LocalDateTime
import retrofit2.http.GET
import javax.inject.Inject
import javax.inject.Named
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey


interface SessionRepository {
    suspend fun getSessions(): List<Session>

    suspend fun getSession(sessionId: String): Session

    suspend fun getBookmarkedSessionIds(): Flow<Set<String>>

    suspend fun bookmarkSession(sessionId: String, bookmark: Boolean)

    suspend fun getCurrentPlayingSession(): Session?

    suspend fun updateCurrentPlayingSession(sessionId: String)
}

internal class DefaultSessionRepository @Inject constructor(
    private val githubRawApi: GithubRawApi,
    private val playbackDataSource: PlaybackPreferencesDataSource,
    private val sessionDataSource: SessionPreferencesDataSource,
) : SessionRepository {
    private var cachedSessions: List<Session> = emptyList()

    private val currentPlayingSessionId: Flow<String?> = playbackDataSource.currentPlayingSessionId
    private val bookmarkIds: Flow<Set<String>> = sessionDataSource.bookmarkedSession

    override suspend fun getSessions(): List<Session> {
        return githubRawApi.getSessions()
            .map { it.toData() }
            .also { cachedSessions = it }

    }

    override suspend fun getSession(sessionId: String): Session {
        val cachedSession = cachedSessions.find { it.id == sessionId }
        if (cachedSession != null) {
            return cachedSession
        }

        return getSessions().find { it.id == sessionId }
            ?: error("Session not found with id: $sessionId")
    }

    override suspend fun getBookmarkedSessionIds(): Flow<Set<String>> {
        return bookmarkIds.filterNotNull()
    }

    override suspend fun bookmarkSession(sessionId: String, bookmark: Boolean) {
        val currentBookmarkedSessionIds = bookmarkIds.first()
        sessionDataSource.updateBookmarkedSession(
            if (bookmark) {
                currentBookmarkedSessionIds + sessionId
            } else {
                currentBookmarkedSessionIds - sessionId
            }
        )
    }

    override suspend fun getCurrentPlayingSession(): Session? {
        return currentPlayingSessionId.firstOrNull()?.let { getSession(it) }
    }

    override suspend fun updateCurrentPlayingSession(sessionId: String) {
        playbackDataSource.updateCurrentPlayingSession(sessionId)
    }
}
internal interface GithubRawApi {

    @GET("/workspace/DroidKnights2023-app-with-media3/media3-main/core/data/src/main/assets/sponsors.json")
    suspend fun getSponsors(): List<SponsorResponse>

    @GET("/workspace/DroidKnights2023-app-with-media3/media3-main/core/data/src/main/assets/sessions.json")
    suspend fun getSessions(): List<SessionResponse>
}
@OptIn(ExperimentalSerializationApi::class)
internal class AssetsGithubRawApi(
    context: Context,
    private val json: Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    },
) : GithubRawApi {
    private val sponsors = context.assets.open("sponsors.json")
    private val sessions = context.assets.open("sessions.json")

    override suspend fun getSponsors(): List<SponsorResponse> {
        return json.decodeFromStream(sponsors)
    }

    override suspend fun getSessions(): List<SessionResponse> {
        return json.decodeFromStream(sessions)
    }
}
@Serializable
internal data class SponsorResponse(
    @SerialName("name") val name: String,
    @SerialName("imageUrl") val imageUrl: String,
    @SerialName("homepage") val homepage: String,
    @SerialName("grade") val grade: Grade,
) {

    enum class Grade {
        @SerialName("platinum")
        PLATINUM,

        @SerialName("gold")
        GOLD,
    }
}
@Serializable
internal data class SessionResponse(
    val id: String,
    val title: String,
    val content: String,
    val speakers: List<SpeakerResponse>,
    val level: LevelResponse,
    val tags: List<String>,
    val room: RoomResponse?,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val video: VideoResponse? = null,
)
@Serializable
data class SpeakerResponse(
    @SerialName("name")
    val name: String,
    @SerialName("introduction")
    val introduction: String,
    @SerialName("imageUrl")
    val imageUrl: String,
)

@Serializable
internal enum class LevelResponse {
    @SerialName("기타")
    ETC,

    @SerialName("초급")
    BASIC,

    @SerialName("중급")
    INTERMEDIATE,

    @SerialName("고급")
    ADVANCED
}
@Serializable
internal enum class RoomResponse {
    ETC,

    @SerialName("Track1")
    TRACK1,

    @SerialName("Track2")
    TRACK2,

    @SerialName("Track3")
    TRACK3
}
@Serializable
internal data class VideoResponse(
    val manifestUrl: String,
    val thumbnailUrl: String,
)
internal fun SessionResponse.toData(): Session = Session(
    id = this.id,
    title = this.title,
    content = this.content,
//    speakers = this.speakers.map { it.toData() },
    level = this.level.toData(),
    tags = this.tags.map { Tag(it) },
    room = this.room?.toData() ?: Room.ETC,
    startTime = this.startTime,
    endTime = this.endTime,
    video = this.video?.toData(),
    isBookmarked = false,
)
internal fun LevelResponse.toData(): Level = when (this) {
    LevelResponse.ETC -> Level.ETC
    LevelResponse.BASIC -> Level.BASIC
    LevelResponse.INTERMEDIATE -> Level.INTERMEDIATE
    LevelResponse.ADVANCED -> Level.ADVANCED
}

internal fun RoomResponse.toData(): Room = when (this) {
    RoomResponse.ETC -> Room.ETC
    RoomResponse.TRACK1 -> Room.TRACK1
    RoomResponse.TRACK2 -> Room.TRACK2
    RoomResponse.TRACK3 -> Room.TRACK3
}

//internal fun SpeakerResponse.toData(): Speaker = Speaker(
//    name = this.name,
//    introduction = this.introduction,
//    imageUrl = this.imageUrl
//)

internal fun VideoResponse.toData(): Video? =

    if (
        manifestUrl.isNotBlank() &&
        thumbnailUrl.isNotBlank()
    ) {
        Video(
            manifestUrl = this.manifestUrl,
            thumbnailUrl = this.thumbnailUrl
        )
    } else {
        null
    }



interface PlaybackPreferencesDataSource {
    val currentPlayingSessionId: Flow<String?>
    suspend fun updateCurrentPlayingSession(sessionId: String)
}

class DefaultPlaybackPreferencesDataSource @Inject constructor(
    @Named("playback") private val dataStore: DataStore<Preferences>
) : PlaybackPreferencesDataSource {

    object PreferencesKey {
        val CURRENT_SESSION_ID = stringPreferencesKey("CURRENT_SESSION_ID")
    }

    override val currentPlayingSessionId: Flow<String?> = dataStore.data.map { preferences ->
        preferences[PreferencesKey.CURRENT_SESSION_ID]
    }

    override suspend fun updateCurrentPlayingSession(sessionId: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.CURRENT_SESSION_ID] = sessionId
        }
    }
}

interface SessionPreferencesDataSource {
    val bookmarkedSession: Flow<Set<String>>
    suspend fun updateBookmarkedSession(bookmarkedSession: Set<String>)
}
class DefaultSessionPreferencesDataSource @Inject constructor(
    @Named("session") private val dataStore: DataStore<Preferences>
) : SessionPreferencesDataSource {
    object PreferencesKey {
        val BOOKMARKED_SESSION = stringSetPreferencesKey("BOOKMARKED_SESSION")
    }

    override val bookmarkedSession = dataStore.data.map { preferences ->
        preferences[PreferencesKey.BOOKMARKED_SESSION] ?: emptySet()
    }

    override suspend fun updateBookmarkedSession(bookmarkedSession: Set<String>) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.BOOKMARKED_SESSION] = bookmarkedSession
        }
    }
}
