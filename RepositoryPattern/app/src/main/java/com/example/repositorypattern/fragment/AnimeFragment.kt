package com.example.repositorypattern.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.repositorypattern.R
import com.example.repositorypattern.databinding.FragmentAnimeBinding
import com.example.repositorypattern.network.response.anime.Anime
import com.example.repositorypattern.utils.AnimeAdapter
import com.example.repositorypattern.view_model.AnimeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        setHasOptionsMenu(false)
        return _binding?.root
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAnime()

        animeViewModel.anime.observe(viewLifecycleOwner, {
            binding.animeNameProgressBar.visibility = View.GONE
            if (hasOptionsMenu()) setHasOptionsMenu(false)
            anime.value = it.toMap()
            animeAdapter = AnimeAdapter(it.toList())
            binding.rvAnime.adapter = animeAdapter

        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.retry_action, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.retry_action) {
            getAnime()
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("RestrictedApi")
    fun getAnime() {
        if (hasOptionsMenu()) setHasOptionsMenu(false)
        lifecycleScope.launch {
            try {
                binding.animeNameProgressBar.visibility = View.VISIBLE
                animeViewModel.getAnime()
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                binding.animeNameProgressBar.visibility = View.GONE
                if (!hasOptionsMenu()) setHasOptionsMenu(true)
            }
        }
    }
}