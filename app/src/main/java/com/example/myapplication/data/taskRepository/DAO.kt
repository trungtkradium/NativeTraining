package com.example.myapplication.data.taskRepository

import androidx.room.*
import com.example.myapplication.data.taskRepository.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAll(): List<Task>?

    @Query("SELECT * FROM tasks WHERE id = (:taskId)")
    fun getTaskById(taskId: String): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg tasks: Task)

    @Update
    fun updateTask(task: Task)

    @Query("DELETE FROM tasks WHERE id = (:taskId)")
    fun deleteById(taskId: String)

    @Query("SELECT * FROM tasks WHERE title = (:taskTitle)")
    fun searchTaskByTitle(taskTitle: String): List<Task>?

}
