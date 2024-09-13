package com.example.alarmpermissiontest

import android.app.AlarmManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.AlarmManagerCompat.canScheduleExactAlarms
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.alarmpermissiontest.media3.AudioQtPlayController
import com.example.alarmpermissiontest.ui.theme.AlarmpermissiontestTheme
import java.net.URL

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val playerController : AudioQtPlayController = AudioQtPlayController(applicationContext)

        enableEdgeToEdge()
        setContent {

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

            }
            // SCHEDULE_EXACT_ALARM


            AlarmpermissiontestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding).clickable {
                            playerController.play()
                        }
                    )
                }
            }


        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AlarmpermissiontestTheme {
        Greeting("Android")
    }
}