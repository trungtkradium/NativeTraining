package com.example.repositorypattern.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.repositorypattern.databinding.AnimeQuoteBinding
import com.example.repositorypattern.network.response.anime_quote.AnimeQuote

class AnimeQuotesAdapter :
    PagingDataAdapter<AnimeQuote, AnimeQuotesViewHolder>(REPO_COMPARATOR) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnimeQuotesViewHolder {
        return AnimeQuotesViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AnimeQuotesViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) holder.bind(item)
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<AnimeQuote>() {
            override fun areItemsTheSame(oldItem: AnimeQuote, newItem: AnimeQuote): Boolean {
                return oldItem.quote === newItem.quote
            }

            override fun areContentsTheSame(oldItem: AnimeQuote, newItem: AnimeQuote): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class AnimeQuotesViewHolder private constructor(private val binding: AnimeQuoteBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AnimeQuote) {
        binding.tvCharacter.text = item.character
        val quote = "\"" + item.quote + "\""
        binding.tvQuote.text = quote
    }

    companion object {
        fun from(parent: ViewGroup): AnimeQuotesViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = AnimeQuoteBinding.inflate(layoutInflater, parent, false)

            return AnimeQuotesViewHolder(binding)
        }
    }
}