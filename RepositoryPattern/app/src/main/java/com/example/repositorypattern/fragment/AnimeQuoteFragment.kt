package com.example.repositorypattern.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.repositorypattern.Constants
import com.example.repositorypattern.R
import com.example.repositorypattern.databinding.FragmentAnimeQuoteBinding
import com.example.repositorypattern.utils.AnimeQuotesAdapter
import com.example.repositorypattern.view_model.AnimeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
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
        setHasOptionsMenu(false)
        return _binding?.root
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animeQuotesAdapter = AnimeQuotesAdapter()
        binding.rvAnimeQuotes.adapter = animeQuotesAdapter
        getQuote()

        animeQuotesAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading){
                binding.animeQuoteProgressBar.visibility = View.VISIBLE
                if (hasOptionsMenu()) setHasOptionsMenu(false)
            }

            else{
                binding.animeQuoteProgressBar.visibility = View.GONE
                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                if (error != null) {
                    Toast.makeText(context, error.error.message, Toast.LENGTH_LONG).show()
                    if (!hasOptionsMenu()) setHasOptionsMenu(true)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.retry_action, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.retry_action) {
            getQuote()
        }
        return true
    }

    @SuppressLint("RestrictedApi")
    private fun getQuote() {
        if (hasOptionsMenu()) setHasOptionsMenu(false)
        lifecycleScope.launch(Dispatchers.IO) {
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