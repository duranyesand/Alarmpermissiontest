package com.example.alarmpermissiontest.media3.playback

import android.content.Context
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.CommandButton
import androidx.media3.session.DefaultMediaNotificationProvider
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService.LibraryParams
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import androidx.media3.session.MediaNotification
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSession.ControllerInfo
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionCommand.COMMAND_CODE_CUSTOM
import androidx.media3.session.SessionError
import androidx.media3.session.SessionResult
import com.example.alarmpermissiontest.R
import com.example.alarmpermissiontest.media3.AudioQtPlayController
import com.example.alarmpermissiontest.media3.NotificationPlayerCustomCommandButton
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.guava.future
import javax.inject.Inject


@UnstableApi
class LibrarySessionCallback @Inject constructor(
    private val mediaItemProvider: MediaItemProvider,
    private val scope: CoroutineScope,
) : MediaLibrarySession.Callback {

    companion object {
        private const val CUSTOM_COMMAND_REWIND_ACTION_ID = "REWIND_15"
        private const val CUSTOM_COMMAND_FORWARD_ACTION_ID = "FAST_FWD_15"
    }

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

    private val notificationPlayerCustomCommandButtons =
        NotificationPlayerCustomCommandButton.values().map { command -> command.commandButton }


    private val customLayoutCommandButtons: List<CommandButton> = listOf(

        CommandButton.Builder(CommandButton.ICON_SKIP_BACK_15)
            .setDisplayName("Rewind")
            .setSessionCommand(SessionCommand(Player.COMMAND_SEEK_FORWARD.toString(), Bundle.EMPTY))
            .build(),

        CommandButton.Builder(CommandButton.ICON_PLAY)
            .setDisplayName("Play/Pause")
            .setSessionCommand(SessionCommand(Player.COMMAND_PLAY_PAUSE.toString(), Bundle.EMPTY))
            .build(),

        CommandButton.Builder(CommandButton.ICON_SKIP_FORWARD_15)
            .setDisplayName("Fast Forward")
            .setSessionCommand(SessionCommand(Player.COMMAND_SEEK_BACK.toString(), Bundle.EMPTY))
            .build()

    )

    val mediaNotificationSessionCommands =
        MediaSession.ConnectionResult.DEFAULT_SESSION_AND_LIBRARY_COMMANDS.buildUpon()
            .also { builder ->
                customLayoutCommandButtons.forEach { commandButton ->
                    commandButton.sessionCommand?.let {
                        builder.add(it)
                    }
                }
            }
            .build()

    override fun onConnect(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
    ): MediaSession.ConnectionResult {
        val connectionResult = super.onConnect(session, controller)

        val availableSessionCommands = connectionResult.availableSessionCommands.buildUpon()

        notificationPlayerCustomCommandButtons.forEach { commandButton ->
            commandButton.sessionCommand?.let(availableSessionCommands::add)
        }

        return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
            .setAvailableSessionCommands(mediaNotificationSessionCommands)
            .setCustomLayout(ImmutableList.copyOf(customLayoutCommandButtons))
            .build()
    }

    override fun onCustomCommand(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        customCommand: SessionCommand,
        args: Bundle
    ): ListenableFuture<SessionResult> {
        return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
    }







    override fun onPlaybackResumption(
        mediaSession: MediaSession,
        controller: ControllerInfo
    ): ListenableFuture<MediaSession.MediaItemsWithStartPosition> {
        Log.d("sflsjfljsfl", "onPlaybackResumption")
        return scope.future {
            mediaItemProvider.currentMediaItemsOrKeynote()
        }
    }
}
