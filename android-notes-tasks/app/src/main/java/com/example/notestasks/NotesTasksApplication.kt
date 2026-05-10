package com.example.notestasks

import android.app.Application
import com.example.notestasks.data.db.AppDatabase
import com.example.notestasks.data.repository.NoteRepository
import com.example.notestasks.data.repository.TaskRepository

class NotesTasksApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val noteRepository by lazy { NoteRepository(database.noteDao()) }
    val taskRepository by lazy { TaskRepository(database.taskDao()) }
}
