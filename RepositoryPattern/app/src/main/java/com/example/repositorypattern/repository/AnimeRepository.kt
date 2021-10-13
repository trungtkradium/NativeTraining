package com.example.repositorypattern.repository

import com.example.repositorypattern.network.AbstractAnimeApi
import com.example.repositorypattern.network.response.anime.Anime
import com.example.repositorypattern.network.response.anime_quote.AnimeQuote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AnimeRepository @Inject constructor(
    private val animeApi: AbstractAnimeApi
): AbstractAnimeRepository {
    override suspend fun getAnime(): Flow<List<Anime>?> = flow {
        val response =
            animeApi.getAnime()
        val listAnime = response.map {
            Anime(it)
        }
        emit(listAnime)
    }

    override suspend fun getAnimeQuotes(title: String, page: Int): List<AnimeQuote> {
        return animeApi.getAnimeQuote(title, page)
    }
}