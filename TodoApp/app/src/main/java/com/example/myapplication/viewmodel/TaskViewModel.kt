package com.example.myapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.myapplication.data.taskRepository.DEFAULT_PAGE_SIZE
import com.example.myapplication.data.taskRepository.Task
import com.example.myapplication.data.taskRepository.TaskPagingSource
import com.example.myapplication.data.taskRepository.TaskRepositoryInterface
import com.example.myapplication.utils.TasksFilterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


class TaskViewModel @Inject constructor(
    var taskRepository: TaskRepositoryInterface
) : ViewModel() {

    var tasks: MutableLiveData<List<Task?>?>? = MutableLiveData<List<Task?>?>()

    init {
        viewModelScope.launch {
            tasks?.value = taskRepository.getAllTask()
        }
    }

    // For Paging function
    fun flow(filterType: TasksFilterType): Flow<PagingData<Task>> {
        return Pager(
            PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = true,
                initialLoadSize = 30,
                maxSize = 200,
            )
        ) {
            TaskPagingSource(taskRepository)
        }.flow
            .map { value: PagingData<Task> ->
                when (filterType) {
                    TasksFilterType.COMPLETED_TASKS -> value.filter { task -> task.isDone }
                    TasksFilterType.INCOMPLETE_TASKS -> value.filter { task -> !task.isDone }
                    else -> value
                }
            }
            .cachedIn(viewModelScope)
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