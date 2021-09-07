package com.example.datalayerexample

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.work.*
import com.example.datalayerexample.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

//val Context.settingsDataStore: DataStore<Settings> by dataStore(
//    fileName = "settings.pb",
//    serializer = SettingsSerializer
//)

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val workManager = WorkManager.getInstance(this)
//    private lateinit var keySaved: Flow<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.etKeySaved.visibility = View.GONE
//        binding.etReadkey.visibility = View.GONE
//        binding.btnRead.visibility = View.GONE
//        keySaved = settingsDataStore.data
//            .map { settings ->
//                settings.keySaved
//            }
//        lifecycleScope.launch {
//            keySaved.collectLatest {
//                if (it.isEmpty()) binding.tvReadValue.text = "No value" else
//                    binding.tvReadValue.text = it
//            }
//        }

        workManager.pruneWork()
        workManager.getWorkInfosByTagLiveData(ACTION_READ).observe(this, { workInfo ->
            val index = workInfo.size
            if (index != 0) {
                val info = workInfo[index - 1]
                when (info.state) {
                    WorkInfo.State.SUCCEEDED -> binding.tvReadValue.text =
                        info.outputData.getString(KEY_RESULT) ?: "No Key found"
                    WorkInfo.State.FAILED -> binding.tvReadValue.text = "Failed, please try again"
                    else -> {
                    }
                }
            }
        })

        workManager.getWorkInfosByTagLiveData(ACTION_SAVE).observe(this, { workInfo ->
            val index = workInfo.size
            if (index != 0) {
                val info = workInfo[index - 1]

                if (info.state == WorkInfo.State.SUCCEEDED) {
                    binding.tvSaveValue.text = ""
                }
                if (info.state == WorkInfo.State.FAILED) {
                    binding.tvSaveValue.text = "Failed, please try again"
                }
            }
        })

        binding.btnSave.setOnClickListener {
            workManager.enqueue(
                OneTimeWorkRequest.Builder(
                    DataStoreWorker::class.java
                )
                    .addTag(ACTION_SAVE)
                    .setInputData(
                        Data.Builder()
                            .putString(KEY_ACTION, ACTION_SAVE)
                            .putString(KEY_KEY_SAVED, binding.etKeySaved.text.toString())
                            .putString(KEY_VALUE_SAVED, binding.etValueSaved.text.toString())
                            .build()
                    )
                    .build()
            )

//            lifecycleScope.launch {
//                saveKey(
//                    binding.etKeySaved.text.toString(),
//                    binding.etValueSaved.text.toString()
//                )
//            }
        }

        binding.btnRead.setOnClickListener {
            workManager.enqueue(
                OneTimeWorkRequest.Builder(
                    DataStoreWorker::class.java
                )
                    .addTag(ACTION_READ)
                    .setInputData(
                        Data.Builder()
                            .putString(KEY_ACTION, ACTION_READ)
                            .putString(KEY_KEY_SAVED, binding.etReadkey.text.toString())
                            .build()
                    )
                    .setConstraints(
                        Constraints.Builder()
//                            .setRequiresBatteryNotLow(true)
                            .build()
                    )
                    .build()
            )

//            lifecycleScope.launch {
//                val value = readKey(binding.etReadkey.text.toString())
//                binding.tvReadValue.text = value ?: "No Key found"
//            }
        }
    }

    private suspend fun saveKey(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
//        settingsDataStore.updateData { currentSettings ->
//            currentSettings.toBuilder()
//                .setKeySaved(value)
//                .build()
//        }
    }

    private suspend fun readKey(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
