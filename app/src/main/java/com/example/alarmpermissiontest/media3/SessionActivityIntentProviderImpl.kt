package com.example.alarmpermissiontest.media3

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import com.example.alarmpermissiontest.MainActivity
import com.example.alarmpermissiontest.media3.playback.SessionActivityIntentProvider
import javax.inject.Inject

class SessionActivityIntentProviderImpl @Inject constructor(
    private val context: Context,
) : SessionActivityIntentProvider {

    override fun toPlayer(): PendingIntent? {

        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.youtube.com/watch?v=gR5SQ01x2Ik"), // Use Uri.parse to convert the string to a URI
            context,
            MainActivity::class.java
        )

        // 작업 스택에 해당 intent를 추가
        val deepLinkPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }
        return deepLinkPendingIntent
    }
}