package com.example.myapplication.data.taskRepository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import java.lang.Exception
import javax.inject.Inject

const val STARTING_PAGE_INDEX = 1
const val DEFAULT_PAGE_SIZE = 10

class TaskPagingSource @Inject constructor(
    var taskRepository: TaskRepositoryInterface
) : PagingSource<Int, Task>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Task> {
        val pageNumber = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = taskRepository.getTaskByPaging(pageNumber, DEFAULT_PAGE_SIZE)
            val nextKey = if (response.isEmpty() || response.size < DEFAULT_PAGE_SIZE) {
                pageNumber
            } else {
                pageNumber + 1
            }

            LoadResult.Page(
                data = response,
                prevKey = if (pageNumber == STARTING_PAGE_INDEX) null else pageNumber - 1,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Task>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}

