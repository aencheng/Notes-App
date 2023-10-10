package com.example.project6

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class EditNotesViewModel(noteId: Long, val dao: NotesDao) : ViewModel() {
    // Retrieve the note from the database using the provided noteId
    val note = dao.get(noteId)

    // MutableLiveData to handle navigation back to the list view
    private val _navigateToList = MutableLiveData<Boolean>(false)
    val navigateToList = _navigateToList

    // Function to update the note in the database
    fun updateNote() {
        viewModelScope.launch {
            dao.update(note.value!!)
            _navigateToList.value = true
        }
    }

    // Function to reset the navigation flag after navigating back to the list view
    fun onNavigatedToList() {
        _navigateToList.value = false
    }
}