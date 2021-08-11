package com.example.myapplication.data.taskRepository

import androidx.room.*
import com.example.myapplication.data.taskRepository.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    suspend fun getAll(): List<Task>?

    @Query("SELECT * FROM tasks WHERE id = (:taskId)")
    suspend fun getTaskById(taskId: String): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg tasks: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("DELETE FROM tasks WHERE id = (:taskId)")
    suspend fun deleteById(taskId: String)

}
