package com.example.notestasks.data.repository

import com.example.notestasks.data.db.TaskDao
import com.example.notestasks.data.model.Task

class TaskRepository(private val dao: TaskDao) {
    val allTasks = dao.getAllTasks()

    fun searchTasks(query: String) = dao.searchTasks(query)

    suspend fun insert(task: Task) = dao.insert(task)

    suspend fun update(task: Task) = dao.update(task)

    suspend fun delete(task: Task) = dao.delete(task)

    suspend fun deleteCompleted() = dao.deleteCompleted()
}
