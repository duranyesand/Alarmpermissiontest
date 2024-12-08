package com.example.alarmpermissiontest.media3

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.alarmpermissiontest.media3.playback.MediaItemProvider
import com.example.alarmpermissiontest.media3.playback.SessionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.guava.asDeferred
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class AudioQtPlayController @Inject constructor (
    private val context : Context,
    private val sessionRepository : SessionRepository,
    private val mediaItemProvider: MediaItemProvider,
) {

    // MediaController 객체를 비동기로 생성한다.
    private var controllerDeferred: Deferred<MediaController> = newControllerAsync()

    @androidx.annotation.OptIn(UnstableApi::class)
    private fun newControllerAsync() = MediaController

        .Builder(context, SessionToken(context, ComponentName(context, AudioQtService::class.java)))

        .buildAsync()

        .asDeferred()

    // 현재 MediaController의 연결 상태 관리 및 새 컨트롤러 생성
    @OptIn(ExperimentalCoroutinesApi::class)
    private val activeControllerDeferred: Deferred<MediaController>
        get() {
            if (controllerDeferred.isCompleted) {
                val completedController = controllerDeferred.getCompleted()
                if (!completedController.isConnected) {
                    completedController.release()
                    controllerDeferred = newControllerAsync()
                }
            }
            return controllerDeferred
        }

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    // 미디어 재생 위치 설정
    fun setPosition(positionMs: Long) = executeAfterPrepare { controller ->
        controller.seekTo(positionMs)
    }
    // 앞으로 감기
    fun fastForward() = executeAfterPrepare { controller ->
        controller.seekForward()
    }
    // 뒤로 감기
    fun rewind() = executeAfterPrepare { controller ->
        controller.seekBack()
    }
    // 이전 트랙 이동
    fun previous() = executeAfterPrepare { controller ->
        controller.seekToPrevious()
    }
    // 다음 트랙 이동
    fun next() = executeAfterPrepare { controller ->
        controller.seekToNext()
    }
    // 재생 시작
    fun play() = executeAfterPrepare { controller ->
        controller.play()
    }
    // 재생 / 일시정지 전환
    fun playPause() = executeAfterPrepare { controller ->
        if (controller.isPlaying) {
            controller.pause()
        } else {
            controller.play()
        }
    }
    // 재생 속도
    fun setSpeed(speed: Float) = executeAfterPrepare { controller ->
        controller.setPlaybackSpeed(speed)
    }
    // MediaController를 준비
    private suspend fun maybePrepare(controller: MediaController): Boolean {
        if (
            controller.playbackState in listOf(Player.STATE_READY, Player.STATE_BUFFERING)
        ) {
            return true
        }
        controller.prepare()
        return true
    }
    // MediaController 준비 후 실행
    private inline fun executeAfterPrepare(crossinline action: suspend (MediaController) -> Unit) {
        scope.launch {
            val controller = awaitConnect() ?: return@launch
            if (maybePrepare(controller)) {
                action(controller)
            }
        }
    }
    // MediaController 연결 대기
    private suspend fun awaitConnect(): MediaController? {
        return runCatching {
            activeControllerDeferred.await()
        }.getOrElse { e ->
            if (e is CancellationException) throw e
            null
        }
    }


}