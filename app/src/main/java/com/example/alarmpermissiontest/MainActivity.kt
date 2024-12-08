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

    private lateinit var alarmManager: AlarmManager


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        enableEdgeToEdge()
        setContent {
//            val playerController : AudioQtPlayController()






            /**
             * 알람 권한 허용 메시지
             * <uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" /> 추가하기
             */
            // SCHEDULE_EXACT_ALARM
            val context = LocalContext.current
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

                val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java)

                if (alarmManager?.canScheduleExactAlarms() == false) {
                    Intent().also { intent ->
                        intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                        intent.data = Uri.parse("package:$packageName")
                        context.startActivity(intent)
                    }
                }
                Log.d("Sfsfsfsfsf" , "package:$packageName")

            }
            // SCHEDULE_EXACT_ALARM


            AlarmpermissiontestTheme {


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

                        val context = LocalContext.current
                        Button(onClick = {
//                            setDailyAlarm(context , 5 , 6)
                            Log.d("QTAlarmReceiver" , "setAlarmSucced")
                        }) {
                            Text(text = "알람 맞추기")
                        }


                        // SessionDetailScreen에서 플레이어 연동
//                        SessionDetailScreen(
//                            sessionId = "1",
//                            onBackClick = {},
//                            onShowPlayer = {
//                                playerController.playAudio("https://www.example.com/audio.mp3")  // 실제 재생할 URL 또는 파일 경로
//                            },
//                        )
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


