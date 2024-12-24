package com.example.alarmpermissiontest.media3.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alarmpermissiontest.PlayerUiState
import com.example.alarmpermissiontest.media3.AudioQT.AudioQtStateManager
import com.example.alarmpermissiontest.media3.AudioQtPlayController
//import com.example.alarmpermissiontest.media3.PlayerRoute
//import com.example.alarmpermissiontest.media3.usecase.UpdateCurrentPlayingSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
//    getCurrentPlayingSessionUseCase: GetCurrentPlayingSessionUseCase,
//    updateCurrentPlayingSessionUseCase: UpdateCurrentPlayingSessionUseCase,
    private val playbackStateManager: AudioQtStateManager,
    private val playerController: AudioQtPlayController,
) : ViewModel() {
    private val _playerUiState =
        MutableStateFlow<PlayerUiState>(PlayerUiState.Loading)
    val playerUiState: StateFlow<PlayerUiState> = _playerUiState

    init {
        viewModelScope.launch {
            playerController.play()
        }
        viewModelScope.launch {
            playbackStateManager.flow.collect {
                _playerUiState.value = PlayerUiState.Success(
                    it.isPlaying,
                    it.hasPrevious,
                    it.hasNext,
                    it.position,
                    it.duration,
                    it.speed,
                    it.aspectRatio
                )
            }
        }
    }

    fun playPause() {
        playerController.playPause()
    }

    fun prev() {
        playerController.previous()
    }

    fun next() {
        playerController.next()
    }

    fun setPosition(position: Long) {
        playerController.setPosition(position)
    }

    // 앞으로 감기
    fun fastForward(position: Long) {
        playerController.fastForward()
    }
    // 뒤로 감기
    fun rewind(position: Long) {
        playerController.rewind()
    }
}