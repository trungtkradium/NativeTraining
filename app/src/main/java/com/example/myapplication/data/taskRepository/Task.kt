package com.example.myapplication.data.taskRepository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tasks")
data class Task (
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "status") val status: Boolean,
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString(),
) {
    val isDone
        get() = status

    val isEmpty
        get() = title.isEmpty()|| description.isEmpty()
}
