package com.example.repositorypattern.di

import com.example.repositorypattern.Constants
import com.example.repositorypattern.network.AbstractAnimeApi
import com.example.repositorypattern.network.RetrofitAnimeApi
import com.example.repositorypattern.repository.AbstractAnimeRepository
import com.example.repositorypattern.repository.AnimeRepository
import com.example.repositorypattern.view_model.AnimeViewModel
import dagger.Binds
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
    fun animeApi(): AbstractAnimeApi {
        return Retrofit.Builder()
            .baseUrl(Constants.ANIME_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitAnimeApi::class.java)
    }

    @Singleton
    @Provides
    fun animeViewModel(animeRepository: AbstractAnimeRepository): AnimeViewModel {
        return AnimeViewModel(animeRepository)
    }
}

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class AbstractRepositoryPatternModule {
    @Binds
    abstract fun bindAnimeRepository(
        animeRepository: AnimeRepository
    ): AbstractAnimeRepository
}
