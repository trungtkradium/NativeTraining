package com.example.repositorypattern.repository

import com.example.repositorypattern.network.AnimeApi
import com.example.repositorypattern.network.Resource
import com.example.repositorypattern.network.response.anime.Anime
import com.example.repositorypattern.network.response.anime_quote.AnimeQuote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AnimeRepository @Inject constructor(
    private val animeRetrofit: AnimeApi
) : BaseRepository() {

    suspend fun getAnime(): Flow<List<Anime>?> = flow {
        val response = safeApiCall {
            animeRetrofit.getAnime()
        }

        when (response) {
            is Resource.Success -> {
                val listAnime = response.data?.map {
                    Anime(it)
                }
                emit(listAnime)
            }
            else -> {
                emit(null)
            }
        }
    }

    suspend fun getAnimeQuotes(title: String, page: Int): List<AnimeQuote> {
        val response = safeApiCall {
            animeRetrofit.getAnimeQuote(title, page)
        }

        return when (response) {
            is Resource.Success -> {
                response.data ?: emptyList()
            }
            else -> {
                emptyList()
            }
        }
    }
}