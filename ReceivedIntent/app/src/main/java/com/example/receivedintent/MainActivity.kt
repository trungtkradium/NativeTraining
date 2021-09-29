package com.example.receivedintent

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.receivedintent.databinding.ActivityMainBinding
import com.example.receivedintent.service.NotificationService

class MainActivity : AppCompatActivity() {


    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        startService(Intent(this, NotificationService::class.java))
        if (intent?.action == Constants.IntentReceived) {
            intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
                startService(Intent(this, NotificationService::class.java).putExtra("title", it))
                binding.textView.text = it;
            }
        }
    }

    override fun onDestroy() {
        stopService(Intent(this, NotificationService::class.java))
        super.onDestroy()
    }
}