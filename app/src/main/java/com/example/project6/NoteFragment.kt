package com.example.project6

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.project6.databinding.FragmentNoteBinding

class NoteFragment : Fragment() {
    private lateinit var binding: FragmentNoteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        val view = binding.root

        // Extract the noteId from the arguments using Safe Args
        val noteId = NoteFragmentArgs.fromBundle(requireArguments()).noteId

        // Get the application context and initialize the database access object (DAO)
        val application = requireNotNull(this.activity).application
        val dao = NoteDatabase.getInstance(application).notesDao

        // Create a ViewModelFactory with the noteId and DAO
        val viewModelFactory = EditNotesViewModelFactory(noteId, dao)

        // Create a ViewModel using the ViewModelProvider with the factory
        val viewModel = ViewModelProvider(this, viewModelFactory)[EditNotesViewModel::class.java]

        // Set the ViewModel for data binding
        binding.viewModel = viewModel

        // Set the lifecycle owner for LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // Add a TextWatcher to update the note name in the ViewModel as the user types
        binding.editTitle.addTextChangedListener{
            viewModel.note.value!!.noteName = binding.editTitle.text.toString()
        }

        // Add a TextWatcher to update the note content in the ViewModel as the user types
        binding.editDescription.addTextChangedListener {
            viewModel.note.value!!.noteContent = binding.editDescription.text.toString()
        }

        // Observe navigation events to the list view
        viewModel.navigateToList.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController().navigate(R.id.action_noteFragment_to_homeFragment)
                viewModel.onNavigatedToList()
            }
        })

        return view
    }
}
