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
//    sfsfsfsfsfsfsefsfe : AudioQtPlayController,
    private val mediaItemProvider: MediaItemProvider,
    private val scope: CoroutineScope,
    private val viewModel: AudioQtPlayController

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


//    override fun onConnect(
//        session: MediaSession,
//        controller: MediaSession.ControllerInfo
//    ): MediaSession.ConnectionResult {
//        val connectionResult = super.onConnect(session, controller)
//        val availableSessionCommands = connectionResult.availableSessionCommands.buildUpon()
//
//        /* Registering custom player command buttons for player notification. */
//        notificationPlayerCustomCommandButtons.forEach { commandButton ->
//            commandButton.sessionCommand?.let(availableSessionCommands::add)
//        }
//
//        return MediaSession.ConnectionResult.accept(
//            availableSessionCommands.build(),
//            connectionResult.availablePlayerCommands
//        )
//    }

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


    //    override fun onCustomCommand(
//        session: MediaSession,
//        controller: MediaSession.ControllerInfo,
//        customCommand: SessionCommand,
//        args: Bundle,
//    ): ListenableFuture<SessionResult> {
//
//        Log.d("sfjlsjflsjflsjflsiejf", "sfljsflsjfls")
//
////        viewModel.fastForward()
////
////        session.setCustomLayout(
////            session.mediaNotificationControllerInfo!!,
////            ImmutableList.of(customLayoutCommandButtons[0]),
////        )
////
////        session.setCustomLayout(
////            session.mediaNotificationControllerInfo!!,
////            ImmutableList.of(customLayoutCommandButtons[1]),
////        )
////
////        session.setCustomLayout(
////            session.mediaNotificationControllerInfo!!,
////            ImmutableList.of(customLayoutCommandButtons[2]),
////        )
//        return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
//    }
    override fun onCustomCommand(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        customCommand: SessionCommand,
        args: Bundle
    ): ListenableFuture<SessionResult> {
//        if (customCommand.customAction == NotificationPlayerCustomCommandButton.REWIND.customAction) {
//            session.player.seekBack()
//        }
//        if (customCommand.customAction == NotificationPlayerCustomCommandButton.FORWARD.customAction) {
//            session.player.seekForward()
//        }
        return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
    }



//    override fun onGetLibraryRoot(
//        session: MediaLibrarySession,
//        browser: ControllerInfo,
//        params: LibraryParams?,
//    ): ListenableFuture<LibraryResult<MediaItem>> {
//        Log.d("sflsjfljsfl", "onGetLibraryRoot")
//
//        if (params?.isRecent == true) {
//            return Futures.immediateFuture(LibraryResult.ofError(SessionError.ERROR_NOT_SUPPORTED))
//        }
//        return Futures.immediateFuture(LibraryResult.ofItem(mediaItemProvider.root(), params))
//    }

//    override fun onGetItem(
//        session: MediaLibrarySession,
//        browser: ControllerInfo,
//        mediaId: String,
//    ): ListenableFuture<LibraryResult<MediaItem>> = scope.future {
//        Log.d("sflsjfljsfl", "onGetItem")
//
//        val item = mediaItemProvider.item(mediaId)
//        if (item != null) {
//            LibraryResult.ofItem(item, null)
//        } else {
//            LibraryResult.ofError(SessionError.ERROR_BAD_VALUE)
//        }
//    }

//    override fun onGetChildren(
//        session: MediaLibrarySession,
//        browser: ControllerInfo,
//        parentId: String,
//        page: Int,
//        pageSize: Int,
//        params: LibraryParams?,
//    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> = scope.future {
//        Log.d("sflsjfljsfl", "onGetChildren")
//        val children = mediaItemProvider.children(parentId)
//        if (children != null) {
//            LibraryResult.ofItemList(children, params)
//        } else {
//            LibraryResult.ofError(SessionError.ERROR_BAD_VALUE)
//        }
//    }

//    override fun onAddMediaItems(
//        mediaSession: MediaSession,
//        controller: ControllerInfo,
//        mediaItems: List<MediaItem>
//    ): ListenableFuture<List<MediaItem>> = scope.future {
//        Log.d("sflsjfljsfl", "onAddMediaItems")
//
//        mediaItems.map { mediaItem ->
//            mediaItemProvider.item(mediaItem.mediaId) ?: mediaItem
//        }
//    }



//    override fun onSubscribe(
//        session: MediaLibrarySession,
//        browser: ControllerInfo,
//        parentId: String,
//        params: LibraryParams?
//    ): ListenableFuture<LibraryResult<Void>> = scope.future {
//        Log.d("sflsjfljsfl", "onSubscribe")
//
//        val children = mediaItemProvider.children(parentId)
//            ?: return@future LibraryResult.ofError(SessionError.ERROR_BAD_VALUE)
//        session.notifyChildrenChanged(browser, parentId, children.size, params)
//        LibraryResult.ofVoid()
//    }

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


@UnstableApi
class CustomMediaNotificationProvider(context: Context) :
    DefaultMediaNotificationProvider(context) {

    override fun addNotificationActions(
        mediaSession: MediaSession,
        mediaButtons: ImmutableList<CommandButton>,
        builder: NotificationCompat.Builder,
        actionFactory: MediaNotification.ActionFactory
    ): IntArray {
        /* Retrieving notification default play/pause button from mediaButtons list. */
        val defaultPlayPauseCommandButton = mediaButtons.getOrNull(0)
        val notificationMediaButtons = if (defaultPlayPauseCommandButton != null) {
            /* Overriding received mediaButtons list to ensure required buttons order: [rewind15, play/pause, forward15]. */
            ImmutableList.builder<CommandButton>().apply {
                add(NotificationPlayerCustomCommandButton.REWIND.commandButton)
                add(defaultPlayPauseCommandButton)
                add(NotificationPlayerCustomCommandButton.FORWARD.commandButton)
            }.build()
        } else {
            /* Fallback option to handle nullability, in case retrieving default play/pause button fails for some reason (should never happen). */
            mediaButtons
        }
        return super.addNotificationActions(
            mediaSession,
            notificationMediaButtons,
            builder,
            actionFactory
        )
    }
}