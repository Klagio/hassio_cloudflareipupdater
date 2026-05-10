package com.example.notestasks.data.repository

import com.example.notestasks.data.db.NoteDao
import com.example.notestasks.data.model.Note

class NoteRepository(private val dao: NoteDao) {
    val allNotes = dao.getAllNotes()

    fun searchNotes(query: String) = dao.searchNotes(query)

    suspend fun getNoteById(id: Long) = dao.getNoteById(id)

    suspend fun insert(note: Note) = dao.insert(note)

    suspend fun update(note: Note) = dao.update(note)

    suspend fun delete(note: Note) = dao.delete(note)
}
