package com.example.myapplication

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.data.taskRepository.Task
import com.example.myapplication.data.taskRepository.mock.MockTaskRepositoryImp
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.jupiter.api.Tag
import org.junit.runner.RunWith
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

    private val mockTaskRepositoryImp = MockTaskRepositoryImp()

    @Test
    fun `Insert New Task`() {
        val insertTask = Task("New Task Title", "New Task Description", false)
        mockTaskRepositoryImp.insert(insertTask)
        val insertedTask = mockTaskRepositoryImp.getTask(insertTask.id)
        assertEquals(insertTask, insertedTask)
        println("Insert New Task Test Successfully")
    }

    @Test
    fun `Update Exist Task`() {
        val insertTask = Task("New Task Title", "New Task Description", false)
        mockTaskRepositoryImp.insert(insertTask)
        mockTaskRepositoryImp.updateTask(
            Task(
                "New Task Title Updated",
                "New Task Description Updated",
                !insertTask.status,
                insertTask.id
            )
        )
        val updatedTask = mockTaskRepositoryImp.getTask(insertTask.id)

        assertEquals("New Task Title Updated", updatedTask?.title)
        assertEquals("New Task Description Updated", updatedTask?.description)
        assertEquals(true, updatedTask?.status)
        println("Update Exist Task Test Successfully")
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
        val updatedTask = mockTaskRepositoryImp.getTask("un-existTask")

        assertEquals(null, updatedTask)
        println("Update Un-exist Task Test Successfully")
    }

    @Test
    fun `Delete Exist Task`() {
        val insertTask = Task("New Task Title", "New Task Description", false)
        mockTaskRepositoryImp.insert(insertTask)
        mockTaskRepositoryImp.deleteById(insertTask.id)
        val deleteTask = mockTaskRepositoryImp.getTask(insertTask.id)
        assertEquals(null, deleteTask)
        println("Delete Exist Task Test Successfully")
    }

    @Test
    fun `Search By Title`() {
        var title = "New Task Title"

        for (i in 0..5) {
            if (i>3) title = title.plus(i)
            mockTaskRepositoryImp.insert(Task(title, title, true))
        }

        val result = mockTaskRepositoryImp.searchByTitle("New Task Title")
        result.forEach {
            assertEquals("New Task Title", it.title)
        }
        assertEquals(4, result.size)
        println("Search By Title Test Successfully")
    }
}