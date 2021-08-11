package com.example.myapplication

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.data.taskRepository.Task
import com.example.myapplication.data.taskRepository.TaskDao
import com.example.myapplication.data.taskRepository.TaskRepositoryImp
import com.example.myapplication.data.taskRepository.mock.MockTaskRepositoryImp
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Tag
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.robolectric.annotation.Config


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
@Tag("TaskUnitTest")
class TaskUnitTest {

    private val mockTaskRepositoryImp = mock(MockTaskRepositoryImp::class.java)
    private val taskDAO = mock(TaskDao::class.java)

    private val mockTasks = listOf(
        Task("1", "1", true, "1"),
        Task("2", "2", true, "2"),
        Task("3", "3", false, "3"),
        Task("4", "4", false, "4"),
    )

    @Before
    fun init() {
        `when`(mockTaskRepositoryImp.mTaskDao).thenReturn(taskDAO)
        `when`(mockTaskRepositoryImp.getAllTask()).thenReturn(mockTasks)
        mockTasks.forEach {
            `when`(mockTaskRepositoryImp.getTask(it.id)).thenReturn(it)
        }
        `when`(mockTaskRepositoryImp.searchByTitle("1")).thenReturn(
            listOf(
                Task(
                    "1",
                    "1",
                    true,
                    "1"
                )
            )
        )
    }

    @Test
    fun `Get All Tasks`() {
        val tasks = mockTaskRepositoryImp.getAllTask()
        verify(mockTaskRepositoryImp).getAllTask()
        assertEquals(mockTasks, tasks)
    }

    @Test
    fun `Insert New Task`() {
        val insertTask = Task("1", "1", true, "1")
        mockTaskRepositoryImp.insert(insertTask)
        verify(mockTaskRepositoryImp).insert(insertTask)

        val insertedTask = mockTaskRepositoryImp.getTask(insertTask.id)
        verify(mockTaskRepositoryImp).getTask(insertTask.id)
        assertEquals(insertTask, insertedTask)
    }

    @Test
    fun `Update Exist Task`() {
        val insertTask = Task("1", "1", true, "1")
        mockTaskRepositoryImp.insert(insertTask)
        verify(mockTaskRepositoryImp).insert(insertTask)
        mockTaskRepositoryImp.updateTask(
            Task(
                "New Task Title Updated",
                "New Task Description Updated",
                !insertTask.status,
                insertTask.id
            )
        )
        verify(mockTaskRepositoryImp).updateTask(Task(
            "New Task Title Updated",
            "New Task Description Updated",
            !insertTask.status,
            insertTask.id
        ))
    }

    @Test
    fun `Update Un-exist Task`() {
        mockTaskRepositoryImp.updateTask(
            Task(
                "New Task Title Updated",
                "New Task Description Updated",
                true,
                "un-existTask"
            )
        )
        verify(mockTaskRepositoryImp).updateTask(Task(
            "New Task Title Updated",
            "New Task Description Updated",
            true,
            "un-existTask"
        ))
        val updatedTask = mockTaskRepositoryImp.getTask("un-existTask")
        verify(mockTaskRepositoryImp).getTask("un-existTask")

        assertEquals(null, updatedTask)
    }

    @Test
    fun `Delete Exist Task`() {
        val insertTask = Task("1", "1", true, "1")
        mockTaskRepositoryImp.insert(insertTask)
        verify(mockTaskRepositoryImp).insert(insertTask)
        mockTaskRepositoryImp.deleteById(insertTask.id)
        verify(mockTaskRepositoryImp).deleteById(insertTask.id)
    }

    @Test
    fun `Search By Title`() {
        val title = "1"
        val result = mockTaskRepositoryImp.searchByTitle(title)
        verify(mockTaskRepositoryImp).searchByTitle(title)
        assertEquals(1, result?.size)
    }
}