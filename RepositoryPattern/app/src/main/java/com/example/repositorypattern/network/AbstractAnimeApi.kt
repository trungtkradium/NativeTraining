package com.example.repositorypattern.network

import com.example.repositorypattern.network.response.anime.AnimeResponse
import com.example.repositorypattern.network.response.anime_quote.AnimeQuote

interface AbstractAnimeApi {
    suspend fun getAnime(): AnimeResponse
    suspend fun getAnimeQuote(title: String,page: Int
    ): ArrayList<AnimeQuote>
}