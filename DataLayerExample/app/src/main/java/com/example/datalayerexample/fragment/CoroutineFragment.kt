package com.example.datalayerexample.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.datalayerexample.databinding.FragmentCoroutineBinding
import com.example.datalayerexample.viewmodel.PreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CoroutineFragment : Fragment() {

    @Inject
    lateinit var preferencesViewModel: PreferencesViewModel

    private var _binding: FragmentCoroutineBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCoroutineBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferencesViewModel.getValue().observe(viewLifecycleOwner, {
            binding.tvReadValue.text = it
        })

        binding.btnSave.setOnClickListener {
            preferencesViewModel.saveKey(binding.etKeySaved.text.toString(), binding.etValueSaved.text.toString())
        }

        binding.btnRead.setOnClickListener {
            preferencesViewModel.readKey(binding.etReadkey.text.toString())
        }
    }
}