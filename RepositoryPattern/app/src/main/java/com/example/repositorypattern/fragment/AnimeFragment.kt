package com.example.repositorypattern.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.repositorypattern.R
import com.example.repositorypattern.databinding.FragmentAnimeBinding
import com.example.repositorypattern.network.response.anime.Anime
import com.example.repositorypattern.utils.AnimeAdapter
import com.example.repositorypattern.view_model.AnimeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AnimeFragment : Fragment() {

    @Inject
    lateinit var animeViewModel: AnimeViewModel

    private var _binding: FragmentAnimeBinding? = null
    private val binding get() = _binding!!

    private val anime: MutableLiveData<Map<String, Anime>> = MutableLiveData(emptyMap())
    private lateinit var animeAdapter: AnimeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnimeBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animeViewModel.anime.observe(viewLifecycleOwner, {
            if (it != null) {
                anime.value = it.toMap()
                animeAdapter = AnimeAdapter(it.toList())
                binding.rvAnime.adapter = animeAdapter
            }
        })
    }
}