package com.example.datalayerexample.workers

import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.datalayerexample.Constants
import com.example.datalayerexample.dataStore
import com.example.datalayerexample.service.PreferencesService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

class DataStoreWorker(
    appContext: Context,
    workerParams: WorkerParameters,
) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val action = inputData.getString(Constants.KEY_ACTION) ?: return Result.failure()
        val key = inputData.getString(Constants.KEY_KEY_SAVED)
        val value = inputData.getString(Constants.KEY_VALUE_SAVED) ?: ""
        when (action) {
            Constants.ACTION_SAVE -> {
                delay(1000)
                val dataStoreKey = stringPreferencesKey(key ?: "")
                applicationContext.dataStore.edit { settings ->
                    settings[dataStoreKey] = value
                }
                return Result.success()
            }
            Constants.ACTION_READ -> {
                applicationContext.startService(Intent(applicationContext, PreferencesService::class.java).putExtra("title", "Loading"))
                val dataStoreKey = stringPreferencesKey(key ?: "")
                val preferences = applicationContext.dataStore.data.first()
                val result = preferences[dataStoreKey] ?: "No Key found"

                delay(1000)
                val output: Data = Data.Builder().putString(Constants.KEY_RESULT, result).build()
                return Result.success(output)
            }
            Constants.KEY_DISPOSE -> {
                applicationContext.startService(
                    Intent(
                        applicationContext,
                        PreferencesService::class.java
                    ).putExtra("title", "Idle")
                )
                return Result.success()
            }
            Constants.KEY_DISPOSE_BATTERY_LOW -> {
                applicationContext.stopService(Intent(applicationContext, PreferencesService::class.java))
                return Result.success()
            }
            else -> return Result.failure()
        }
    }
}
