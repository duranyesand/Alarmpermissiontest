package com.example.alarmpermissiontest.media3

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// AudioQt State를 Flow로 관리
class AudioQtStateManager {

    private val _audioQtState = MutableStateFlow(AudioQtState())

    val flow : StateFlow<AudioQtState> get() = _audioQtState

    var audioQtState: AudioQtState
        set(value) {
            _audioQtState.value = value
        }
        get() = _audioQtState.value

}