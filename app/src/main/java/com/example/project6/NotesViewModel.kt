package com.example.project6

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

public suspend fun <T> LiveData<T>.await(): T {
    // A utility function to await a LiveData value using coroutines.
    return withContext(Dispatchers.Main.immediate) {
        suspendCancellableCoroutine { continuation ->
            // Execute the following code in the coroutine context.

            val observer = object : Observer<T> {
                override fun onChanged(value: T) {
                    // When the LiveData value changes...
                    removeObserver(this) // Remove the observer to avoid memory leaks.
                    continuation.resume(value) // Resume the coroutine with the value.
                }
            }
            observeForever(observer) // Observe LiveData changes forever.

            // If the coroutine is cancelled, remove the observer to avoid memory leaks.
            continuation.invokeOnCancellation {
                removeObserver(observer)
            }
        }
    }
}

class NotesViewModel(val dao: NotesDao) : ViewModel() {

    var newNoteName = "New Note"
    var newNoteContent = ""

    val notes = dao.getAll()
    // LiveData of a list of notes retrieved from the DAO.

    private val _navigateToNote = MutableLiveData<Long?>()
    val navigateToNote = _navigateToNote
    // MutableLiveData for navigation to a specific note.

    fun addNote() {
        // Function to add a new note to the database.
        viewModelScope.launch {
            // Launch a coroutine in the ViewModel's scope.

            val notes = Note()
            notes.noteName = newNoteName
            notes.noteContent = newNoteContent

            val id = dao.insert(notes) // Insert the note and get its ID.

            _navigateToNote.value = id // Set the navigation value to the new note's ID.
        }
    }

    fun deleteNote(noteId: Long) {
        // Function to delete a note from the database.
        viewModelScope.launch {
            // Launch a coroutine in the ViewModel's scope.

            val notes = dao.get(noteId).await()
            // Suspend until the LiveData has a value, then get the note.

            dao.delete(notes) // Delete the note from the database.
            _navigateToNote.value = null // Reset the navigation value.
        }
    }

    fun onNoteClicked(noteId: Long) {
        // Function called when a note is clicked.
        _navigateToNote.value = noteId // Set the navigation value to the clicked note's ID.
    }

    fun onNoteNavigated() {
        // Function called when navigation to a note is complete.
        _navigateToNote.value = null // Reset the navigation value.
    }
}
