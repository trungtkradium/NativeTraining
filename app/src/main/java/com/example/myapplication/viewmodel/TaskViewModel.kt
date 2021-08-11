package com.example.myapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.taskRepository.Task
import com.example.myapplication.data.taskRepository.TaskRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    var taskRepository: TaskRepositoryInterface
) : ViewModel() {

    var tasks: MutableLiveData<List<Task?>?>? = MutableLiveData<List<Task?>?>()

    init {
        viewModelScope.launch {
            tasks?.value = taskRepository.getAllTask()
        }
    }

    private suspend fun load() {
        tasks?.value = taskRepository.getAllTask()
    }

    fun saveTask(
        taskId: String,
        newTitle: String,
        newDescription: String,
        newStatus: Boolean = false
    ): Int {
        if (Task(newTitle, newDescription, newStatus).isEmpty) {
            return 0
        }

        return if (taskId.isEmpty()) {
            viewModelScope.launch {
                taskRepository.insert(Task(newTitle, newDescription, newStatus))
                load()
            }
            1
        } else {
            val task = Task(newTitle, newDescription, newStatus, taskId)
            viewModelScope.launch {
                taskRepository.updateTask(task)
                load()
            }
            1
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            taskRepository.deleteById(taskId)
            load()
        }
    }
}