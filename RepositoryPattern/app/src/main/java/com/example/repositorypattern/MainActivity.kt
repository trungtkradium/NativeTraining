package com.example.repositorypattern

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.repositorypattern.view_model.AnimeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var animeViewModel: AnimeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        animeViewModel.getAnime()
        setContentView(R.layout.activity_main)
    }
}