package com.example.datalayerexample.viewmodel

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datalayerexample.dataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val value: MutableLiveData<String> = MutableLiveData("")

    fun getValue(): MutableLiveData<String> {
        return value
    }

    fun saveKey(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        viewModelScope.launch(Dispatchers.IO) {
            context.dataStore.edit { settings ->
                settings[dataStoreKey] = value
            }
        }
    }

    fun readKey(key: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val preferences = context.dataStore.data.first()
            value.postValue(preferences[stringPreferencesKey(key)] ?: "No Key found")
        }
    }
}