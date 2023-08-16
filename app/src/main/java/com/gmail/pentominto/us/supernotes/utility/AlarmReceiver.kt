package com.gmail.pentominto.us.supernotes.utility

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.getActivity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gmail.pentominto.us.supernotes.R

class AlarmReceiver : BroadcastReceiver() {

    private var notificationManager : NotificationManagerCompat? = null

    @Throws(SecurityException::class)
    override fun onReceive(context : Context?, intent : Intent?) {
        val idExtra = intent?.getIntExtra("note_id",
            0
        )
        val titleExtra = intent?.getStringExtra("note_title")
        val bodyExtra = intent?.getStringExtra("note_body")

        idExtra?.let { id ->

            val tapResultIntent = Intent().setAction(Intent.ACTION_VIEW).setData(
                Uri.parse("myapp://supernotes/noteeditscreen/$id")
            )

            val pendingIntent : PendingIntent = getActivity(
                context,
                id,
                tapResultIntent,
                FLAG_IMMUTABLE
            )

            val notification = context?.let {
                NotificationCompat.Builder(it,
                    "1"
                )
                    .setContentTitle(titleExtra)
                    .setContentText(bodyExtra)
                    .setSmallIcon(R.drawable.notification_note)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .build()
            }

            notificationManager = context?.let { NotificationManagerCompat.from(it) }
            notification?.let {
                notificationManager?.notify(id,
                    it
                )
            }
        }
    }
}
