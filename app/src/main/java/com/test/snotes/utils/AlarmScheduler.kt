package com.test.snotes.utils


import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.annotation.RequiresPermission
import java.text.SimpleDateFormat
import java.util.Locale

object AlarmScheduler {

    fun scheduleAlarmFromFormattedDate(
        context: Context,
        noteId: String,
        formattedDateTime: String,
        title: String,
        description: String
    ) {
        try {
            val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
            val date = formatter.parse(formattedDateTime)
            if (date != null) {
                val timeInMillis = date.time

                // Optional: check if selected time is in the future
                if (timeInMillis > System.currentTimeMillis()) {
                    scheduleAlarm(context, noteId, timeInMillis, title, description)
                } else {
                    Toast.makeText(context, "Cannot schedule alarm in the past: $formattedDateTime", Toast.LENGTH_SHORT).show()
                  }
            } else {
                Toast.makeText(context, "Failed to parse date: $formattedDateTime", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Invalid date format: $formattedDateTime", Toast.LENGTH_SHORT).show()
       }
    }


    @SuppressLint("ScheduleExactAlarm")
    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    fun scheduleAlarm(context: Context, noteId: String, timeInMillis: Long, title: String, description: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("description", description)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            noteId.hashCode(), // unique per note
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )



        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
        )
    }

    fun cancelAlarm(context: Context, noteId: String) {
        val alarmIntent = PendingIntent.getBroadcast(
            context,
            noteId.hashCode(), // use same request code as when scheduling
            Intent(context, AlarmReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(alarmIntent)
    }

}
