package com.example.alarmpermissiontest.media3

import android.content.Intent
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import com.example.alarmpermissiontest.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import javax.inject.Inject


private const val CUSTOM_COMMAND_REWIND_ACTION_ID = "REWIND_15"
private const val CUSTOM_COMMAND_FORWARD_ACTION_ID = "FAST_FWD_15"

enum class NotificationPlayerCustomCommandButton(
    val customAction: String,
    val commandButton: CommandButton,
) {
    REWIND(
        customAction = CUSTOM_COMMAND_REWIND_ACTION_ID,
        commandButton = CommandButton.Builder()
            .setDisplayName("Rewind")
            .setSessionCommand(SessionCommand(CUSTOM_COMMAND_REWIND_ACTION_ID, Bundle()))
            .setIconResId(R.drawable.ic_launcher_background)
            .build(),
    ),
    FORWARD(
        customAction = CUSTOM_COMMAND_FORWARD_ACTION_ID,
        commandButton = CommandButton.Builder()
            .setDisplayName("Forward")
            .setSessionCommand(SessionCommand(CUSTOM_COMMAND_FORWARD_ACTION_ID, Bundle()))
            .setIconResId(R.drawable.ic_launcher_background)
            .build(),
    );
}

@UnstableApi
@AndroidEntryPoint
class AudioQtService : MediaLibraryService() {

    @Inject
    lateinit var session: MediaLibrarySession

    @Inject
    lateinit var scope: CoroutineScope

    // 미디어 플레이어
    @Inject
    lateinit var player: Player


    //    private val customMediaNotificationProvider: CustomMediaNotificationProvider = CustomMediaNotificationProvider(context = context)
    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        // 미디어 플레이어 초기화
        player = ExoPlayer.Builder(this).build()
//        player


    }

    // 앱종료 또는 Task에서 제거할 대 호출
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        if (!player.playWhenReady) {
            release()
            stopSelf()
        }
    }

    // player , session 해제 , 코루틴 범위 해제
    private fun release() {
        player.release()
        session.release()
        scope.cancel()
    }

    // 모든 리소스 해제
    override fun onDestroy() {
        super.onDestroy()
        release()
    }

    // 클라이너트가 연결 할 때 호출 : MediaSession을 통해서 미디어 제어 가능
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        return session.takeUnless { session ->
            session.invokeIsReleased
        }
    }

}


private val MediaSession.invokeIsReleased: Boolean
    get() = try {

        MediaSession::class.java.getDeclaredMethod("isReleased")
            .apply { isAccessible = true }
            .invoke(this) as Boolean
    } catch (e: Exception) {
        false
    }


