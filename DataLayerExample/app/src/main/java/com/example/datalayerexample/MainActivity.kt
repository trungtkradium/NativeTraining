package com.example.datalayerexample

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.datalayerexample.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dataStore")
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
//
//        lifecycleScope.launch {
//            keySaved.collectLatest {
//                if (it.isEmpty()) binding.tvReadValue.text = "No value" else
//                    binding.tvReadValue.text = it
//            }
//        }

        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
                saveKey(
                    binding.etKeySaved.text.toString(),
                    binding.etValueSaved.text.toString()
                )
            }
        }

        binding.btnRead.setOnClickListener {
            lifecycleScope.launch {
                val value = readKey(binding.etReadkey.text.toString())
                binding.tvReadValue.text = value ?: "No Key found"
            }
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
