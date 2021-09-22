package com.example.datalayerexample.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.os.SystemClock.elapsedRealtime
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.datalayerexample.Constants
import com.example.datalayerexample.databinding.FragmentAlarmManagerBinding
import com.example.datalayerexample.workers.AlarmReceiver
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AlarmManagerFragment : Fragment() {

    @Inject
    lateinit var alarmManager: AlarmManager

    private var _binding: FragmentAlarmManagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlarmManagerBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFireAMRTC.setOnClickListener {
            val intent = Intent(activity, AlarmReceiver::class.java)
            intent.action = Constants.ALARM_MANAGER_ACTION
            intent.putExtra("title", "AlarmManager Demo RTC")
            val pendingIntent = PendingIntent.getBroadcast(activity, 0, intent, 0)
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 20)
                set(Calendar.MINUTE, 52)
            }
            alarmManager.setExact(AlarmManager.RTC, calendar.timeInMillis, pendingIntent)
        }

        binding.btnFireAMERL.setOnClickListener {
            val intent = Intent(activity, AlarmReceiver::class.java)
            intent.action = Constants.ALARM_MANAGER_ACTION
            intent.putExtra("title", "AlarmManager Demo elapsedRealtime")
            val pendingIntent = PendingIntent.getBroadcast(activity, 0, intent, 0)
            val currentTimeERL = elapsedRealtime() + 5000
            alarmManager.setExact(AlarmManager.RTC, currentTimeERL, pendingIntent)
        }
    }
}