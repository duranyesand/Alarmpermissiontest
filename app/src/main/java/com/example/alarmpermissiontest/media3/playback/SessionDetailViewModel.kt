package com.example.alarmpermissiontest.media3.playback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alarmpermissiontest.media3.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.combine

@HiltViewModel
class SessionDetailViewModel @Inject constructor(
    private val getSessionUseCase: GetSessionUseCase,
    private val getBookmarkedSessionIdsUseCase: GetBookmarkedSessionIdsUseCase,
    private val bookmarkSessionUseCase: BookmarkSessionUseCase,
) : ViewModel() {

    private val _sessionUiState =
        MutableStateFlow<SessionDetailUiState>(SessionDetailUiState.Loading)
    val sessionUiState: StateFlow<SessionDetailUiState> = _sessionUiState

    private val _sessionUiEffect = MutableStateFlow<SessionDetailEffect>(SessionDetailEffect.Idle)
    val sessionUiEffect: StateFlow<SessionDetailEffect> = _sessionUiEffect

    init {
        viewModelScope.launch {
            combine(
                sessionUiState,
                getBookmarkedSessionIdsUseCase(),
            ) { sessionUiState, bookmarkIds ->
                when (sessionUiState) {
                    is SessionDetailUiState.Loading -> sessionUiState
                    is SessionDetailUiState.Success -> {
                        sessionUiState.copy(bookmarked = bookmarkIds.contains(sessionUiState.session.id))
                    }
                }
            }.collect { _sessionUiState.value = it }
        }
    }

    fun fetchSession(sessionId: String) {
        viewModelScope.launch {
            val session = getSessionUseCase(sessionId)
            _sessionUiState.value = SessionDetailUiState.Success(session)
        }
    }

    fun toggleBookmark() {
        val uiState = sessionUiState.value
        if (uiState !is SessionDetailUiState.Success) {
            return
        }
        viewModelScope.launch {
            val bookmark = uiState.bookmarked
            bookmarkSessionUseCase(uiState.session.id, !bookmark)
            _sessionUiEffect.value = SessionDetailEffect.ShowToastForBookmarkState(!bookmark)
        }
    }

    fun hidePopup() {
        viewModelScope.launch {
            _sessionUiEffect.value = SessionDetailEffect.Idle
        }
    }
}


sealed interface SessionDetailUiState {

    object Loading : SessionDetailUiState

    data class Success(val session: Session, val bookmarked: Boolean = false) : SessionDetailUiState
}

sealed interface SessionDetailEffect {
    object Idle : SessionDetailEffect
    data class ShowToastForBookmarkState(val bookmarked: Boolean) : SessionDetailEffect
}


class GetSessionUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
) {

    suspend operator fun invoke(sessionId: String): Session {
        return sessionRepository.getSession(sessionId)
    }
}
class GetBookmarkedSessionIdsUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
) {

    suspend operator fun invoke(): Flow<Set<String>> {
        return sessionRepository.getBookmarkedSessionIds()
    }
}
class BookmarkSessionUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
) {

    suspend operator fun invoke(sessionId: String, bookmark: Boolean) {
        return sessionRepository.bookmarkSession(sessionId, bookmark)
    }
}