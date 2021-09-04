package com.example.myapplication.data.taskRepository.mock

import android.content.Context
import com.example.myapplication.data.taskRepository.Task
import com.example.myapplication.data.taskRepository.TaskDao
import com.example.myapplication.data.taskRepository.TaskDatabase

class MockTaskRepositoryImp constructor(app: Context) {
    val mTaskDao: TaskDao

     fun getAllTask(): List<Task>? {
        return mTaskDao.getAll()
    }

     fun getTask(taskId: String): Task? {
        return mTaskDao.getTaskById(taskId)
    }

     fun updateTask(task: Task) {
        mTaskDao.updateTask(task)
    }

     fun insert(task: Task) {
        mTaskDao.insertAll(task)
    }

     fun deleteById(taskId: String?) {
        if (taskId != null) mTaskDao.deleteById(taskId)
    }

     fun searchByTitle(title: String): List<Task>? {
        return mTaskDao.searchTaskByTitle(title)
    }

    init {
        val db: TaskDatabase? = app.let { TaskDatabase.getDatabase(it) }
        mTaskDao = db?.taskDao()!!
    }
}