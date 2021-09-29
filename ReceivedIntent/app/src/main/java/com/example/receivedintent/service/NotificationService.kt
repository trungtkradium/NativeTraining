package com.example.receivedintent.service

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.example.receivedintent.Constants
import com.example.receivedintent.MainActivity
import com.example.receivedintent.R

class NotificationService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        createNotificationChannel()
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val title = intent?.getStringExtra("title") ?: "Idle"
        showNotification(title)
        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                Constants.CHANNEL_ID,
                "Received Intent Example",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun showNotification(title: String) {
        val notificationIntent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, 0
        )

        val notification = Notification
            .Builder(this, Constants.CHANNEL_ID)
            .setContentText(title)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(Constants.PREF_NOTIFICATION_ID, notification)
    }
}