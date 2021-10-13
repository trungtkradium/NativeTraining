package com.example.repositorypattern.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.repositorypattern.Constants
import com.example.repositorypattern.network.response.anime_quote.AnimeQuote
import com.example.repositorypattern.repository.AbstractAnimeRepository
import com.example.repositorypattern.repository.AnimeRepository

class AnimeQuotesPagingSource(
    private val title: String,
    private val animeRepository: AbstractAnimeRepository,
) : PagingSource<Int, AnimeQuote>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnimeQuote> {
        try {
            val pageNumber = params.key ?: Constants.STARTING_PAGE_INDEX
            val response = animeRepository.getAnimeQuotes(title, pageNumber)
            val nextKey = if (response.isEmpty() || response.size < Constants.DEFAULT_PAGE_SIZE) {
                null
            } else {
                pageNumber + 1
            }

            return LoadResult.Page(
                data = response,
                prevKey = if (pageNumber == Constants.STARTING_PAGE_INDEX) null else pageNumber - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AnimeQuote>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}

