package com.example.myapplication.data.taskRepository

import android.content.Context
import kotlinx.coroutines.delay
import javax.inject.Inject

class TaskRepositoryImp @Inject constructor(app: Context) : TaskRepositoryInterface {

    override val mTaskDao: TaskDao

    override suspend fun getAllTask(): List<Task>? {
        return mTaskDao.getAll()
    }

    override suspend fun getTask(taskId: String): Task? {
        return mTaskDao.getTaskById(taskId)
    }

    override suspend fun getTaskByPaging(page: Int, pageSize: Int): List<Task> {
        return mTaskDao.getTaskByPaging(page, pageSize)
    }

    override suspend fun updateTask(task: Task) {
        mTaskDao.updateTask(task)
    }

    override suspend fun insert(task: Task) {
        mTaskDao.insertAll(task)
    }

    override fun closeDatabase(app: Context) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteById(taskId: String?) {
        if (taskId != null) mTaskDao.deleteById(taskId)
    }

    init {
        val db: TaskDatabase? = app.let { TaskDatabase.getDatabase(it) }
        mTaskDao = db?.taskDao()!!
    }
}