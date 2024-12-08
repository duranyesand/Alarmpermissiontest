package com.example.alarmpermissiontest.media3.playback

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.alarmpermissiontest.R
import com.example.alarmpermissiontest.media3.Session

import kotlinx.coroutines.delay

@Composable
internal fun SessionDetailScreen(
    sessionId: String,
    onBackClick: () -> Unit,
    onShowPlayer: () -> Unit,
    viewModel: SessionDetailViewModel = hiltViewModel(),
) {
    val scrollState = rememberScrollState()
    val sessionUiState by viewModel.sessionUiState.collectAsStateWithLifecycle()
    val effect by viewModel.sessionUiEffect.collectAsStateWithLifecycle()

    val context = LocalContext.current

//    LaunchedEffect(effect) {
//        if (effect is SessionDetailEffect.ShowToastForBookmarkState) {
//            sendWidgetUpdateCommand(context)
//        }
//    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceDim)
            .systemBarsPadding()
            .verticalScroll(scrollState),
    ) {
        SessionDetailTopAppBar(
            bookmarked = (sessionUiState as? SessionDetailUiState.Success)?.bookmarked ?: false,
            onClickBookmark = { viewModel.toggleBookmark() },
            onBackClick = onBackClick
        )
        Box {
            SessionDetailContent(
                uiState = sessionUiState,
                onPlayButtonClick = {
                    onShowPlayer()
                }
            )
            if (effect is SessionDetailEffect.ShowToastForBookmarkState) {
                SessionDetailBookmarkStatePopup(
                    bookmarked = (effect as SessionDetailEffect.ShowToastForBookmarkState).bookmarked
                )
            }
        }
    }

    LaunchedEffect(sessionId) {
        viewModel.fetchSession(sessionId)
    }

    LaunchedEffect(effect) {
        if (effect is SessionDetailEffect.ShowToastForBookmarkState) {
            delay(1000L)
            viewModel.hidePopup()
        }
    }
}

@Composable
private fun SessionDetailTopAppBar(
    bookmarked: Boolean,
    onClickBookmark: (Boolean) -> Unit,
    onBackClick: () -> Unit,
) {
    KnightsTopAppBar(
        titleRes = R.string.media_session_keynote_title,
        navigationIconContentDescription = null,
        navigationType = TopAppBarNavigationType.Back,
        actionButtons = {
            BookmarkToggleButton(
                bookmarked = bookmarked,
                onClickBookmark = onClickBookmark
            )
        },
        onNavigationClick = onBackClick,
    )
}

@Composable
private fun SessionDetailContent(
    uiState: SessionDetailUiState,
    onPlayButtonClick: () -> Unit,
) {
    when (uiState) {
        is SessionDetailUiState.Loading -> SessionDetailLoading()
        is SessionDetailUiState.Success -> SessionDetailContent(uiState.session, onPlayButtonClick)
    }
}

@Composable
private fun SessionDetailLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun SessionDetailContent(
    session: Session,
    onPlayButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        SessionDetailTitle(title = session.title, modifier = Modifier.padding(top = 8.dp))
        Spacer(modifier = Modifier.height(8.dp))
//        SessionChips(session = session)

        if (session.content.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            SessionOverview(content = session.content)
        }
        Spacer(modifier = Modifier.height(40.dp))
        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)
        Spacer(modifier = Modifier.height(40.dp))

//        SessionDetailSpeaker(session.speakers.first())
        Button(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            enabled = session.video != null,
            onClick = onPlayButtonClick
        ) {
            Text(
                if (session.video != null) "재생하기" else "영상 미제공 세션"
            )
        }
    }
}

//@Composable
//private fun SessionChips(session: Session) {
//    Row(
//        horizontalArrangement = Arrangement.spacedBy(8.dp),
//        verticalAlignment = Alignment.CenterVertically,
//    ) {
//        TrackChip(room = session.room)
//        TimeChip(dateTime = session.startTime)
//        TagChips(tags = session.tags.toPersistentList())
//    }
//}

//@Composable
//private fun TagChips(tags: PersistentList<Tag>) {
//    tags.forEach { tag ->
//        TagChip(tag = tag)
//    }
//}

//@Composable
//private fun TagChip(tag: Tag) {
//    TextChip(
//        text = tag.name,
//        containerColor = DarkGray,
//        labelColor = LightGray,
//    )
//}

@Composable
private fun SessionDetailTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier.padding(end = 64.dp),
        text = title,
        color = MaterialTheme.colorScheme.onSecondaryContainer,
    )
}

//@Composable
//private fun SessionDetailSpeaker(
//    speaker: Speaker,
//    modifier: Modifier = Modifier,
//) {
//    Column(modifier = modifier) {
//        NetworkImage(
//            imageUrl = speaker.imageUrl,
//            modifier = Modifier
//                .size(108.dp)
//                .clip(CircleShape),
//            placeholder = painterResource(id = com.droidknights.app2023.core.ui.R.drawable.placeholder_speaker)
//        )
//
//        Spacer(Modifier.height(16.dp))
//
//        Text(
//            text = "stringResource(id = R.string.session_detail_speaker)",
//            color = MaterialTheme.colorScheme.onSecondaryContainer,
//        )
//        Text(
//            text = speaker.name,
//            color = MaterialTheme.colorScheme.onSecondaryContainer,
//        )
//
//        Spacer(Modifier.height(16.dp))
//
//        Text(
//            text = speaker.introduction,
//            color = MaterialTheme.colorScheme.onSecondaryContainer,
//        )
//    }
//}

@Composable
private fun SessionOverview(content: String) {
    Text(
        text = content,
        color = MaterialTheme.colorScheme.onSecondaryContainer
    )
}

@Composable
private fun BookmarkToggleButton(
    bookmarked: Boolean,
    onClickBookmark: (Boolean) -> Unit
) {
    IconToggleButton(
        checked = bookmarked,
        onCheckedChange = onClickBookmark
    ) {
        Icon(
            painter =
            if (bookmarked) {
                painterResource(id = R.drawable.ic_launcher_background)
            } else {
                painterResource(id = R.drawable.ic_launcher_background)
            },
            contentDescription = null
        )
    }
}



//class SessionDetailContentProvider : PreviewParameterProvider<Session> {
//    override val values: Sequence<Session> = sequenceOf(
//        SampleSessionNoContent,
//        SampleSessionHasContent
//    )
//}


//fun sendWidgetUpdateCommand(context: Context) {
//    context.sendBroadcast(
//        Intent(
//            context,
//            DroidKnightsWidgetReceiver::class.java
//        ).setAction(
//            AppWidgetManager.ACTION_APPWIDGET_UPDATE
//        )
//    )
//}

@Composable
internal fun SessionDetailBookmarkStatePopup(bookmarked: Boolean) {
    val messageStringRes = if (bookmarked) {
        "R.string.session_detail_bookmark_popup_message"
    } else {
        "R.string.session_detail_unbookmark_popup_message"
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(4.dp),
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 15.dp),
            text = "stringResource(id = messageStringRes)",
            color = Color.White
        )
    }
}



@Composable
fun KnightsTopAppBar(
    @StringRes titleRes: Int,
    navigationIconContentDescription: String?,
    modifier: Modifier = Modifier,
    navigationType: TopAppBarNavigationType = TopAppBarNavigationType.Back,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    containerColor: Color = MaterialTheme.colorScheme.surfaceDim,
    actionButtons: @Composable () -> Unit = {},
    onNavigationClick: () -> Unit = {},
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        val icon: @Composable (Modifier, imageVector: ImageVector) -> Unit =
            { modifier, imageVector ->
                IconButton(
                    onClick = onNavigationClick,
                    modifier = modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = navigationIconContentDescription,
                    )
                }
            }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(containerColor)
                .pointerInput(Unit) { /* no-op */ }
                .then(modifier)
        ) {
            if (navigationType == TopAppBarNavigationType.Back) {
                icon(
                    Modifier.align(Alignment.CenterStart),
                    Icons.Filled.ArrowBack
                )
            }
            Row(Modifier.align(Alignment.CenterEnd)) {
                actionButtons()
                if (navigationType == TopAppBarNavigationType.Close) {
                    icon(
                        Modifier,
                        Icons.Filled.Close
                    )
                }
            }
            Text(
                text = stringResource(id = titleRes),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

enum class TopAppBarNavigationType { Back, Close }

@Preview
@Composable
private fun KnightsTopAppBarPreviewBack() {
    KnightsTopAppBar(
        titleRes = android.R.string.untitled,
        navigationType = TopAppBarNavigationType.Back,
        navigationIconContentDescription = "Navigation icon"
    )
}

@Preview
@Composable
private fun KnightsTopAppBarPreviewClose() {
    KnightsTopAppBar(
        titleRes = android.R.string.untitled,
        navigationType = TopAppBarNavigationType.Close,
        navigationIconContentDescription = "Navigation icon"
    )
}


