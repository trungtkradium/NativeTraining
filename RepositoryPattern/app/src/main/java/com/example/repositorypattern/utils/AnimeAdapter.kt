package com.example.repositorypattern.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.repositorypattern.Constants
import com.example.repositorypattern.R
import com.example.repositorypattern.databinding.AnimeBinding
import com.example.repositorypattern.network.response.anime.Anime


class AnimeAdapter(private val dataSet: List<Pair<String, Anime>>) :
    RecyclerView.Adapter<AnimeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        return AnimeViewHolder.from(parent)
    }

    override fun onBindViewHolder(viewHolder: AnimeViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size
}

class AnimeViewHolder private constructor(private val binding: AnimeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Pair<String, Anime>) {
        binding.tvAnime.text = item.first
        binding.root.setOnClickListener {
            binding.root.findNavController().navigate(
                R.id.action_AnimeFragment_to_AnimeQuoteFragment, bundleOf(
                    Constants.ARG_PARAM to item.first
                )
            )
        }
    }

    companion object {
        fun from(parent: ViewGroup): AnimeViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = AnimeBinding.inflate(layoutInflater, parent, false)

            return AnimeViewHolder(binding)
        }
    }
}