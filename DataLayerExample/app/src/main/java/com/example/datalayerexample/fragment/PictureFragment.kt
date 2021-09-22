package com.example.datalayerexample.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.datalayerexample.Constants
import com.example.datalayerexample.databinding.FragmentPictureBinding

class PictureFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = PictureFragment()
    }

    private var _binding: FragmentPictureBinding? = null
    private val binding get() = _binding!!


    var launchSomeActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data?.flags == Constants.REQUEST_CAMERA_CODE) {
                    val imageBitmap = data.extras?.get("data") as Bitmap
                    imageBitmap.let {
                        binding.imageView.setImageBitmap(it)
                    }
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPictureBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnTakePicture.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureIntent.flags = Constants.REQUEST_CAMERA_CODE
            launchSomeActivity.launch(takePictureIntent)
        }
    }
}