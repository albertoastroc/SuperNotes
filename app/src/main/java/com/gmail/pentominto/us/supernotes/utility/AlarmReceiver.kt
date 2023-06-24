package com.gmail.pentominto.us.supernotes.utility

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.PendingIntent.getActivity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.gmail.pentominto.us.supernotes.R
import com.gmail.pentominto.us.supernotes.activities.mainactivity.MainActivity
import com.gmail.pentominto.us.supernotes.data.Note

class AlarmReceiver : BroadcastReceiver() {
    private var notificationManager: NotificationManagerCompat? = null

    //"myapp://compose/noteeditscreen/{${taskInfo?.noteId}}".toUri()

    @SuppressLint("MissingPermission")
    override fun onReceive(p0: Context?, p1: Intent?) {
        val taskInfo = p1?.getSerializableExtra("task_info") as? Note
        // tapResultIntent gets executed when user taps the notification
        val tapResultIntent = Intent(Intent.ACTION_VIEW, "myapp://supernotes/noteeditscreen/{${taskInfo?.noteId}}".toUri(), p0, MainActivity::class.java)
        tapResultIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent: PendingIntent = getActivity( p0,0,tapResultIntent,FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)

        val notification = p0?.let {
            NotificationCompat.Builder(it, "1")
                .setContentTitle("Task Reminder")
                .setContentText("Content Text")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build()
        }
        notificationManager = p0?.let { NotificationManagerCompat.from(it) }
        notification?.let { taskInfo?.let { todd -> notificationManager?.notify(todd.noteId, it) } }
    }


}