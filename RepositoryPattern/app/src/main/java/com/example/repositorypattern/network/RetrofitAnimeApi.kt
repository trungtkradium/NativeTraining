package com.example.repositorypattern.network

import com.example.repositorypattern.network.response.anime.AnimeResponse
import com.example.repositorypattern.network.response.anime_quote.AnimeQuote
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitAnimeApi: AbstractAnimeApi {
    @GET("api/available/anime")
    override suspend fun getAnime(): AnimeResponse

    @GET("api/quotes/anime")
    override suspend fun getAnimeQuote(
        @Query("title") title: String,
        @Query("page") page: Int
    ): ArrayList<AnimeQuote>
}