package com.example.datalayerexample.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.work.*
import com.example.datalayerexample.Constants
import com.example.datalayerexample.databinding.FragmentWorkManagerBinding
import com.example.datalayerexample.service.PreferencesService
import com.example.datalayerexample.workers.DataStoreWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class WorkManagerFragment : Fragment() {

    @Inject
    lateinit var workManager: WorkManager

    @Inject
    lateinit var preferencesService: PreferencesService

    private var _binding: FragmentWorkManagerBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWorkManagerBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSave.setOnClickListener {
            binding.tvSaveValue.text = "Loading"
            saveKey(binding.etKeySaved.text.toString(), binding.etValueSaved.text.toString())
        }

        binding.btnRead.setOnClickListener {
            binding.tvReadValue.text = ""
            readValue(binding.etReadkey.text.toString())
        }

        binding.btnStartService.setOnClickListener {
            activity?.startService(
                Intent(
                    activity,
                    PreferencesService::class.java
                )
            )
        }

        binding.btnStopService.setOnClickListener {
            activity?.stopService(Intent(activity, PreferencesService::class.java))
        }
    }

    private fun startServiceWithTitle(title: String) {
        activity?.startService(
            Intent(activity, PreferencesService::class.java).putExtra(
                "title",
                title
            )
        )
    }

    private fun saveKey(key: String, value: String) {
        val workRequest =
            OneTimeWorkRequest.Builder(
                DataStoreWorker::class.java
            )
                .setInputData(
                    Data.Builder()
                        .putString(Constants.KEY_ACTION, Constants.ACTION_SAVE)
                        .putString(Constants.KEY_KEY_SAVED, key)
                        .putString(Constants.KEY_VALUE_SAVED, value)
                        .build()
                )
                .build()
        workManager.enqueue(
            workRequest
        )
        workManager.getWorkInfoByIdLiveData(workRequest.id)
            .observe(viewLifecycleOwner, { workInfo ->
                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    binding.tvSaveValue.text = ""
                }
                if (workInfo.state == WorkInfo.State.FAILED) {
                    binding.tvSaveValue.text = "Failed, please try again"
                }
            })
    }

    private fun readValue(key: String) {
        val workRequest = OneTimeWorkRequest.Builder(
            DataStoreWorker::class.java
        )
            .setInputData(
                Data.Builder()
                    .putString(Constants.KEY_ACTION, Constants.ACTION_READ)
                    .putString(Constants.KEY_KEY_SAVED, key)
                    .build()
            )
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .build()
        workManager.enqueue(
            workRequest
        )
        workManager.getWorkInfoByIdLiveData(workRequest.id)
            .observe(viewLifecycleOwner, { workInfo ->
                when (workInfo.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        binding.tvReadValue.text =
                            workInfo.outputData.getString(Constants.KEY_RESULT) ?: "No Value found"
                        startServiceWithTitle(binding.tvReadValue.text.toString())
                    }

                    WorkInfo.State.FAILED -> {
                        binding.tvReadValue.text = "Failed, please try again"
                        startServiceWithTitle("Idle")
                    }
                    else -> {
                    }
                }
            })
    }

    override fun onDestroy() {
        val workRequest = OneTimeWorkRequest.Builder(
            DataStoreWorker::class.java
        )
            .setInitialDelay(1000, TimeUnit.MILLISECONDS)
            .setInputData(
                Data.Builder()
                    .putString(Constants.KEY_ACTION, Constants.KEY_DISPOSE)
                    .build()
            )
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .build()
        val workRequestIfBatteryLow = OneTimeWorkRequest.Builder(
            DataStoreWorker::class.java
        )
            .setInitialDelay(3000, TimeUnit.MILLISECONDS)
            .setInputData(
                Data.Builder()
                    .putString(Constants.KEY_ACTION, Constants.KEY_DISPOSE_BATTERY_LOW)
                    .build()
            )
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .build()
        workManager.beginWith(listOf(workRequest, workRequestIfBatteryLow)).enqueue()
        super.onDestroy()
    }
}