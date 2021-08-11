package com.example.myapplication.data.taskRepository.mock

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.data.taskRepository.Task
import com.example.myapplication.data.taskRepository.TaskDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class MockTaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    fun closeDatabase() {
        INSTANCE?.close()
        INSTANCE = null
    }

    companion object {
        @Volatile
        private var INSTANCE: MockTaskDatabase? = null
        private const val NUMBER_OF_THREADS = 4
        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(
            NUMBER_OF_THREADS
        )

        fun getDatabase(context: Context): MockTaskDatabase? {
            if (INSTANCE == null) {
                synchronized(MockTaskDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            MockTaskDatabase::class.java, "mock_task_database"
                        ).allowMainThreadQueries().build()
                    }
                }
            }
            return INSTANCE
        }
    }
}