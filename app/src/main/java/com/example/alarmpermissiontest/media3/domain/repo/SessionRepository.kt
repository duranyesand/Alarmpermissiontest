package com.example.alarmpermissiontest.media3.domain.repo

import com.example.alarmpermissiontest.media3.domain.model.Session
import kotlinx.coroutines.flow.Flow

interface SessionRepository {

    suspend fun getSessions(): List<Session>

    suspend fun getSession(sessionId: String): Session

    suspend fun getCurrentPlayingSession(): Session?

    suspend fun updateCurrentPlayingSession(sessionId: String)

}