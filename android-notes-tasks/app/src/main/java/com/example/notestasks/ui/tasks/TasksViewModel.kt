package com.example.notestasks.ui.tasks

import androidx.lifecycle.*
import com.example.notestasks.data.model.Task
import com.example.notestasks.data.repository.TaskRepository
import kotlinx.coroutines.launch

class TasksViewModel(private val repository: TaskRepository) : ViewModel() {

    private val searchQuery = MutableLiveData<String>("")

    val tasks: LiveData<List<Task>> = searchQuery.switchMap { query ->
        if (query.isBlank()) repository.allTasks
        else repository.searchTasks(query)
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun insert(task: Task) = viewModelScope.launch {
        repository.insert(task)
    }

    fun toggleCompleted(task: Task) = viewModelScope.launch {
        repository.update(task.copy(isCompleted = !task.isCompleted))
    }

    fun delete(task: Task) = viewModelScope.launch {
        repository.delete(task)
    }

    fun deleteCompleted() = viewModelScope.launch {
        repository.deleteCompleted()
    }
}

class TasksViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TasksViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
