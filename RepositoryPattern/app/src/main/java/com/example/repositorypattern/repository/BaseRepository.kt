package com.example.repositorypattern.repository

import com.example.repositorypattern.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepository {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (exception: Exception) {
                println(exception)
                Resource.Error(exception.message ?: "Something's wrong. Try again!")
            }
        }
    }
}