package com.example.repositorypattern.di

import com.example.repositorypattern.Constants
import com.example.repositorypattern.network.AnimeApi
import com.example.repositorypattern.repository.AnimeRepository
import com.example.repositorypattern.view_model.AnimeViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryPatternModule {

    @Singleton
    @Provides
    fun animeRetrofit(): AnimeApi {
        return Retrofit.Builder()
            .baseUrl(Constants.ANIME_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AnimeApi::class.java)
    }

    @Singleton
    @Provides
    fun animeRepository(animeRetrofit: AnimeApi): AnimeRepository {
        return AnimeRepository(animeRetrofit)
    }

    @Singleton
    @Provides
    fun animeViewModel(animeRepository: AnimeRepository): AnimeViewModel {
        return AnimeViewModel(animeRepository)
    }
}