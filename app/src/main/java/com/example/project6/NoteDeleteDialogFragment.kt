package com.example.project6

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider


class NoteDeleteDialogFragment(noteId: Long) : DialogFragment() {
    val noteId = noteId
    // Stores the note ID provided when creating an instance of this fragment.

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        // Overrides the onCreateDialog method to create a custom dialog.

        AlertDialog.Builder(requireContext())
            // Creates a new AlertDialog.Builder associated with the current context.

            .setMessage("Are you sure you want to delete?")
            // Sets the message to display in the dialog.

            .setPositiveButton("Delete") { _,_ ->
                // Sets a positive button and its click listener.

                val application = requireNotNull(this.activity).application
                // Gets the application context.

                val dao = NoteDatabase.getInstance(application).notesDao
                // Initializes the DAO for accessing the notes database.

                val viewModelFactory = NotesViewModelFactory(dao)
                // Creates a ViewModel factory with the DAO.

                val viewModel = ViewModelProvider(this, viewModelFactory)[NotesViewModel::class.java]
                // Creates a ViewModel using the ViewModelProvider with the factory.

                viewModel.deleteNote(noteId)
                // Calls the deleteNote function in the ViewModel to delete the note with the specified ID.
            }

            .setNegativeButton("Cancel"){ _,_ -> }
            // Sets a negative button with no action (dismisses the dialog).

            .create()
    // Creates and returns the AlertDialog.

    companion object {
        const val TAG = "NoteDeleteDialogFragment"
        // Defines a constant TAG for identifying this fragment.
    }
}
