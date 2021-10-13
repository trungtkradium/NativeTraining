package com.example.repositorypattern.view_model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.repositorypattern.Constants
import com.example.repositorypattern.network.response.anime.Anime
import com.example.repositorypattern.network.response.anime_quote.AnimeQuote
import com.example.repositorypattern.paging.AnimeQuotesPagingSource
import com.example.repositorypattern.repository.AbstractAnimeRepository
import com.example.repositorypattern.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class AnimeViewModel @Inject constructor(
    private val animeRepository: AbstractAnimeRepository
) : ViewModel() {
    private val _anime: MutableLiveData<MutableMap<String, Anime>> =
        MutableLiveData<MutableMap<String, Anime>>()

    val anime get() = _anime

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun getAnime() {
        animeRepository.getAnime().collectLatest {
            val tempValue = _anime.value ?: mutableMapOf()
            it?.forEach { element ->
                tempValue.putIfAbsent(element.anime, element)
            }
            _anime.postValue(tempValue)
        }

    }

    fun getQuotesByTitle(title: String): Flow<PagingData<AnimeQuote>> {
        return Pager(
            PagingConfig(
                pageSize = Constants.DEFAULT_PAGE_SIZE,
                enablePlaceholders = true,
                initialLoadSize = 10,
                maxSize = 100,
            )
        ) {
            AnimeQuotesPagingSource(title, animeRepository)
        }.flow.map {
            it.map { element ->
                var tempQuote = _anime.value?.get(title)?.quotes
                tempQuote = tempQuote?.plus(element)
                _anime.value?.put(title, Anime(title, tempQuote!!))
                element
            }
        }
            .cachedIn(viewModelScope)
    }
}