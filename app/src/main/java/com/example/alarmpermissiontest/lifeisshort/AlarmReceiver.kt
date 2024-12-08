//package com.example.alarmpermissiontest.lifeisshort
//
//import android.app.AlarmManager
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.net.Uri
//import android.os.Build
//import android.provider.Settings
//import android.util.Log
//import androidx.core.app.NotificationCompat
//import androidx.core.content.ContextCompat
//import com.example.alarmpermissiontest.R
//import java.util.Calendar
//
//class AlarmReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context?, intent: Intent) {
//        when (intent.action) {
//            "triggerQTAlarm" -> {
//                Log.d("QTAlarmReceiver", "triggerQTAlarm")
//                notifyQTAlarm(context!!)
////                setDailyAlarm(context!! , hour = 2 , minute = 33 )
//            }
//
//            "stopQTAlarm" -> {
//                Log.d("QTAlarmReceiver", "stopQTAlarm")
//
//            }
//
//            Intent.ACTION_BOOT_COMPLETED -> {
//                Log.d("QTAlarmReceiver", "ACTION_BOOT_COMPLETED")
//
//            }
//
//            else -> {
//                Log.d("QTAlarmReceiver", "else")
//
//            }
//        }
//    }
//}
//
//
//fun notifyQTAlarm(context: Context) {
//    val notificationManager =
//        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//    val notificationChannelIntId = 777
//    val notificationChannelStringId = "QTAlarmId"
//    val notificationNAme = "QTAlarmName"
//
//    // 알림 채널 생성 (Android 8.0 이상에서 필요)
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        val channel = NotificationChannel(
//            // id
//            notificationChannelStringId,
//            // name
//            notificationNAme,
//            // importance
//            NotificationManager.IMPORTANCE_HIGH
//        )
//        notificationManager.createNotificationChannel(channel)
//    }
//
//    // NotificationCompat.Builder를 통해 알림 생성
//    val notificationBuilded = NotificationCompat.Builder(context, notificationChannelStringId)
//        .setSmallIcon(R.drawable.ic_launcher_background)
//        .setContentTitle("알람")
//        .setContentText("정해진 시간에 알람이 울렸습니다.")
//        .setPriority(NotificationCompat.PRIORITY_HIGH)
//        .build()
//
//    // 알림 표시
//    notificationManager.notify(notificationChannelIntId, notificationBuilded)
//}
//
//fun setDailyAlarm(context: Context, hour: Int, minute: Int) {
//    // AlarmManager 인스턴스 가져오기
//    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//    // 알람이 실행될 시간 설정
//    val calendar = Calendar.getInstance().apply {
//        set(Calendar.HOUR_OF_DAY, hour)
//        set(Calendar.MINUTE, minute)
//        set(Calendar.SECOND, 0)
//        set(Calendar.MILLISECOND, 0)
//
//        // 현재 시간보다 이른 시간이 설정되었다면, 다음 날로 알람을 설정
//        if (before(Calendar.getInstance())) {
//            add(Calendar.DATE, 1)
//        }
//    }
//
//    // 알람 실행 시 호출할 BroadcastReceiver에 대한 PendingIntent 설정
//    val intent = Intent(context, AlarmReceiver::class.java).apply {
//        action = "triggerQTAlarm"
//    }
//    val pendingIntent = PendingIntent.getBroadcast(
//        context,
//        0,
//        intent,
//        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//    )
//
//    // 매일 같은 시간에 알람을 울리도록 설정
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//
//
//        if (alarmManager?.canScheduleExactAlarms() == true) {
//            alarmManager.setExactAndAllowWhileIdle(
//                AlarmManager.RTC_WAKEUP,
//                calendar.timeInMillis,
////        AlarmManager.INTERVAL_DAY,
//                pendingIntent
//            )
//
//        } else {
//            Log.d("QTAlarmReceiver" , "canScheduleExactAlarms == false")
//        }
//
//    }
//
//
////    alarmManager.setA
//}