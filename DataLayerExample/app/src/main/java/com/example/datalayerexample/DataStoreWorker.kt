package com.example.datalayerexample

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

const val KEY_RESULT = "result"
const val KEY_KEY_SAVED = "keySaved"
const val KEY_VALUE_SAVED = "valueSaved"
const val KEY_ACTION = "action"
const val ACTION_SAVE = "save"
const val ACTION_READ = "read"


class DataStoreWorker(
    appContext: Context,
    workerParams: WorkerParameters,
) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val action = inputData.getString(KEY_ACTION) ?: return Result.failure()
        val key = inputData.getString(KEY_KEY_SAVED) ?: return Result.failure()
        val value = inputData.getString(KEY_VALUE_SAVED) ?: ""

        if (key.isEmpty()) return Result.failure()

        if (action == ACTION_SAVE) {
            val dataStoreKey = stringPreferencesKey(key)
            runBlocking {
                applicationContext.dataStore.edit { settings ->
                    settings[dataStoreKey] = value
                }
            }
            return Result.success()
        }

        if (action == ACTION_READ) {
            var result: String
            runBlocking {
                val dataStoreKey = stringPreferencesKey(key ?: "")
                val preferences = applicationContext.dataStore.data.first()
                result = preferences[dataStoreKey] ?: "No Key found"
            }
            val output: Data = Data.Builder().putString(KEY_RESULT, result).build()
            return Result.success(output)
        }

        return Result.failure()
    }
}
