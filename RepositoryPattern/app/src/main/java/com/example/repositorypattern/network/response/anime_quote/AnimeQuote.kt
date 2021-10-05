package com.example.repositorypattern.network.response.anime_quote

import com.google.gson.annotations.SerializedName

data class AnimeQuote(
        @SerializedName("anime")
        val anime: String,
        @SerializedName("character")
        val character: String,
        @SerializedName("quote")
        val quote: String
)