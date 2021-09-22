package com.example.datalayerexample.workers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.datalayerexample.Constants
import com.example.datalayerexample.service.PreferencesService

class AlarmReceiver: BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Constants.ALARM_MANAGER_ACTION) {
            val title = intent.getStringExtra("title")
            context?.startForegroundService(
                Intent(context, PreferencesService::class.java).putExtra(
                    "title",
                    title
                )
            )
        }
    }
}