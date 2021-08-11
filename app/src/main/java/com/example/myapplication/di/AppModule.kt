package com.example.myapplication.di

import android.content.Context
import com.example.myapplication.data.taskRepository.TaskRepositoryImp
import com.example.myapplication.data.taskRepository.TaskRepositoryInterface
import com.example.myapplication.viewmodel.TaskViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TaskModule {

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext app:Context): Context {
        return app
    }

    @Singleton
    @Provides
    fun taskViewModel(taskRepositoryInterface: TaskRepositoryInterface): TaskViewModel {
        return TaskViewModel(taskRepositoryInterface)
    }
}

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class TaskRepositoryModule {
    @Binds
    abstract fun bindTaskRepository(
        taskRepositoryImpl: TaskRepositoryImp
    ): TaskRepositoryInterface
}

