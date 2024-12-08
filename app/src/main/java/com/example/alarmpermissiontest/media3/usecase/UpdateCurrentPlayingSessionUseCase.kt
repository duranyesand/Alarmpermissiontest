package com.example.alarmpermissiontest.media3.usecase

import com.example.alarmpermissiontest.media3.playback.SessionRepository
import javax.inject.Inject

class UpdateCurrentPlayingSessionUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
) {

    suspend operator fun invoke(sessionId: String) {
        return sessionRepository.updateCurrentPlayingSession(sessionId)
    }
}
