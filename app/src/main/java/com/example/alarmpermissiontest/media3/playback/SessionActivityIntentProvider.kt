package com.example.alarmpermissiontest.media3.playback

import android.app.PendingIntent

interface SessionActivityIntentProvider {
    fun toPlayer(): PendingIntent?
}