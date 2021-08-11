package com.example.myapplication.data.taskRepository.mock

import com.example.myapplication.data.taskRepository.Task

class MockTaskRepositoryImp {

    val tasks: MutableList<Task> = mutableListOf()

    fun getTask(taskId: String): Task? {
        val result = tasks.filter { it.id == taskId }
        return if (result.isEmpty()) null; else result[0];
    }

    fun updateTask(task: Task) {
        tasks.forEachIndexed { index, it ->
            if (it.id == task.id) {
                tasks[index] = task
            }
        }
    }

    fun insert(task: Task) {
        tasks.add(task)
    }

    fun deleteById(taskId: String) {
        tasks.removeIf { it.id == taskId }
    }

    fun searchByTitle(title: String): List<Task> {
        return tasks.filter { it.title == title }
    }
}