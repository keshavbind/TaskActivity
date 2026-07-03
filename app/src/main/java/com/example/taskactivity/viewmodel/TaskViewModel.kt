package com.example.taskactivity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskactivity.database.Task
import com.example.taskactivity.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel(){

    fun insert(task: Task){
        viewModelScope.launch {
            repository.insert(task)
        }
    }

    fun delete(task: Task){
        viewModelScope.launch {
            repository.delete(task)
        }
    }
    fun update(task: Task){
        viewModelScope.launch {
            repository.update(task)
        }
    }
    val allTasks=repository.allTasks

}