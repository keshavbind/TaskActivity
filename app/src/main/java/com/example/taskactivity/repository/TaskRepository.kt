package com.example.taskactivity.repository

import androidx.room.Query
import com.example.taskactivity.database.Task
import com.example.taskactivity.database.TaskDao

class TaskRepository(private val dao:TaskDao) {

    suspend fun insert(task: Task){
        dao.insert(task)
    }

    suspend fun delete(task: Task){
        dao.delete(task)
    }

    suspend fun update(task: Task){
        dao.update(task)
    }

    val allTasks=dao.getAllTasks()

}