package com.example.alarmpermissiontest

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alarmpermissiontest.media3.viewmodel.PlayerViewModel
import com.example.alarmpermissiontest.ui.theme.AlarmpermissiontestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                val viemodsfsf: PlayerViewModel = hiltViewModel()
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 버튼 클릭 시 플레이어로 음악 재생
                    Button(onClick = {
                        viemodsfsf.rewind(15)
                    }) {
                        Text(text = "재생하기")
                    }


                }


            }
        }


    }
}


sealed interface PlayerUiState {

    object Loading : PlayerUiState

    data class Success(
        val isPlaying: Boolean,
        val hasPrevious: Boolean,
        val hasNext: Boolean,
        val position: Long,
        val duration: Long,
        val speed: Float,
        val aspectRatio: Float
    ) : PlayerUiState
}


