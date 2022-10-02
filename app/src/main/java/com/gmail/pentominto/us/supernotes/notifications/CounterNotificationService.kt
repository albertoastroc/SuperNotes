package com.gmail.pentominto.us.supernotes.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.gmail.pentominto.us.supernotes.MainActivity
import com.gmail.pentominto.us.supernotes.R

class CounterNotificationService(
    private val context : Context
) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(string : String) {

        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(
            context,
            REMINDER_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_iconexported)
            .setContentTitle("Title")
            .setContentText("Text")
            .setContentIntent(activityPendingIntent).build()

        notificationManager.notify(1, notification)

    }

    companion object {

        const val REMINDER_CHANNEL_ID = "reminder_channel_id"
    }
}