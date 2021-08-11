package com.example.myapplication.data.taskRepository

import android.content.Context
import com.example.myapplication.data.taskRepository.Task
import com.example.myapplication.data.taskRepository.TaskDao

interface TaskRepositoryInterface {
    val mTaskDao: TaskDao

    suspend fun getAllTask(): List<Task>?

    suspend fun getTask(taskId: String): Task?

    suspend fun updateTask(task: Task)

    suspend fun insert(task: Task)

    fun closeDatabase(app: Context)

    suspend fun deleteById(taskId: String?)
}