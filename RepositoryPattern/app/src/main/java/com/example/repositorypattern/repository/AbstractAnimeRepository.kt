package com.example.repositorypattern.repository

import com.example.repositorypattern.network.response.anime.Anime
import com.example.repositorypattern.network.response.anime_quote.AnimeQuote
import kotlinx.coroutines.flow.Flow

interface AbstractAnimeRepository {
    suspend fun getAnime(): Flow<List<Anime>?>
    suspend fun getAnimeQuotes(title: String, page: Int) : List<AnimeQuote>
}