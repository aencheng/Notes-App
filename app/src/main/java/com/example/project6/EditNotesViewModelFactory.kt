package com.example.project6
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EditNotesViewModelFactory(private val noteId: Long,
                                private val dao: NotesDao
)
    : ViewModelProvider.Factory {

      override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditNotesViewModel::class.java)) {
            return EditNotesViewModel(noteId, dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}