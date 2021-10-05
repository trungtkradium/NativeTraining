package com.example.repositorypattern.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.repositorypattern.Constants
import com.example.repositorypattern.databinding.FragmentAnimeBinding
import com.example.repositorypattern.databinding.FragmentAnimeQuoteBinding
import com.example.repositorypattern.utils.AnimeQuotesAdapter
import com.example.repositorypattern.view_model.AnimeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AnimeQuoteFragment : Fragment() {

    @Inject
    lateinit var animeViewModel: AnimeViewModel

    private var _binding: FragmentAnimeQuoteBinding? = null
    private val binding get() = _binding!!

    private var animeTitle: String? = null
    private lateinit var animeQuotesAdapter: AnimeQuotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            animeTitle = it.getString(Constants.ARG_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnimeQuoteBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animeQuotesAdapter = AnimeQuotesAdapter()
        binding.rvAnimeQuotes.adapter = animeQuotesAdapter
        lifecycleScope.launch {
            animeViewModel.getQuotesByTitle(animeTitle!!).collectLatest { pagingData ->
                animeQuotesAdapter.submitData(pagingData)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(animeTitle: String) =
            AnimeQuoteFragment().apply {
                arguments = Bundle().apply {
                    putString(Constants.ARG_PARAM, animeTitle)
                }
            }
    }
}