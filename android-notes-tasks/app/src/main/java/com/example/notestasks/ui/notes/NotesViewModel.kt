package com.example.notestasks.ui.notes

import androidx.lifecycle.*
import com.example.notestasks.data.model.Note
import com.example.notestasks.data.repository.NoteRepository
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NoteRepository) : ViewModel() {

    private val searchQuery = MutableLiveData<String>("")

    val notes: LiveData<List<Note>> = searchQuery.switchMap { query ->
        if (query.isBlank()) repository.allNotes
        else repository.searchNotes(query)
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun insert(note: Note) = viewModelScope.launch {
        repository.insert(note)
    }

    fun update(note: Note) = viewModelScope.launch {
        repository.update(note)
    }

    fun delete(note: Note) = viewModelScope.launch {
        repository.delete(note)
    }
}

class NotesViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
