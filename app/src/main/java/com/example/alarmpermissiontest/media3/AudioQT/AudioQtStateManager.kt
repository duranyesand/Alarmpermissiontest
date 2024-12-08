package com.example.alarmpermissiontest.media3.AudioQT

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Singleton

// AudioQt State를 Flow로 관리
@Singleton
class AudioQtStateManager {

    private val _audioQtState = MutableStateFlow(AudioQtState())

    val flow : StateFlow<AudioQtState> get() = _audioQtState

    var audioQtState: AudioQtState
        set(value) {
            _audioQtState.value = value
        }
        get() = _audioQtState.value

}