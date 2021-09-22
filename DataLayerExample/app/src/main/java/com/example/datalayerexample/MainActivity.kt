package com.example.datalayerexample

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import com.example.datalayerexample.fragment.AlarmManagerFragment
import com.example.datalayerexample.fragment.CoroutineFragment
import com.example.datalayerexample.fragment.PictureFragment
import com.example.datalayerexample.fragment.WorkManagerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val _coroutineFragment = CoroutineFragment()
    private val _workManagerFragment = WorkManagerFragment()
    private val _alarmManagerFragment = AlarmManagerFragment()
    private val _pictureFragment = PictureFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(_coroutineFragment)

        val view = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        view.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.coroutine -> replaceFragment(_coroutineFragment)
                R.id.workManager -> replaceFragment(_workManagerFragment)
                R.id.TakePicture -> replaceFragment(_pictureFragment)
                else -> replaceFragment(_alarmManagerFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment?) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }
}
