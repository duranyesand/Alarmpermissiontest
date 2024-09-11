package com.example.alarmpermissiontest.media3.domain.repo

import com.example.alarmpermissiontest.media3.domain.model.Session

class DefaultSessionRepository : SessionRepository {
    override suspend fun getSessions(): List<Session> {
        TODO("Not yet implemented")
    }

    override suspend fun getSession(sessionId: String): Session {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentPlayingSession(): Session? {
        TODO("Not yet implemented")
    }

    override suspend fun updateCurrentPlayingSession(sessionId: String) {
        TODO("Not yet implemented")
    }
}