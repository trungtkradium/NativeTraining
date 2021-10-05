package com.example.repositorypattern.network.response.anime

import com.example.repositorypattern.network.response.anime_quote.AnimeQuote

data class Anime(val anime: String, val quotes: List<AnimeQuote> = emptyList())