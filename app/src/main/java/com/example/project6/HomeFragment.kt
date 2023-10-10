package com.example.project6

import android.os.Bundle
import android.text.Layout.Directions
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.project6.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        // Get the application context and initialize the database access object (DAO)
        val application = requireNotNull(this.activity).application
        val dao = NoteDatabase.getInstance(application).notesDao

        // Create a ViewModelFactory with the DAO
        val viewModelFactory = NotesViewModelFactory(dao)

        // Create a ViewModel using the ViewModelProvider with the factory
        val viewModel = ViewModelProvider(this, viewModelFactory)[NotesViewModel::class.java]

        // Set the ViewModel for data binding
        binding.viewModel = viewModel

        // Set the lifecycle owner for LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner


        // Initialize an adapter for the RecyclerView, with click and delete callbacks
        val adapter = ItemAdapter(
            { noteId: Long -> run {
                viewModel.onNoteClicked(noteId)
            }},
            { noteId: Long -> run {
                // Show a dialog to confirm note deletion
                val dialog = NoteDeleteDialogFragment(noteId)
                dialog.show(childFragmentManager, NoteDeleteDialogFragment.TAG)
            }}
        )
        binding.itemRecyclerView.adapter = adapter

        // Observe changes in the list of notes and update the adapter
        viewModel.notes.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        // Observe navigation events to a specific note
        viewModel.navigateToNote.observe(viewLifecycleOwner) { noteId ->
            noteId?.let {
                // Navigate to the NoteFragment with the selected note ID
                val action = HomeFragmentDirections.actionHomeFragmentToNoteFragment(noteId)
                this.findNavController().navigate(action)

                // Reset the navigation event
                viewModel.onNoteNavigated()
            }
        }

        return view
    }
}