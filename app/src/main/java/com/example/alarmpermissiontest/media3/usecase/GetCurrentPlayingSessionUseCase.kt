package com.example.alarmpermissiontest.media3.usecase

import com.example.alarmpermissiontest.media3.Session
import com.example.alarmpermissiontest.media3.playback.SessionRepository
import javax.inject.Inject

class GetCurrentPlayingSessionUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
) {
    suspend operator fun invoke(): Session? {
        return sessionRepository.getCurrentPlayingSession()
    }
}