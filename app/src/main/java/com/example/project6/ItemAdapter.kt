package com.example.project6

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.project6.databinding.ItemNoteBinding


class NoteDiffItemCallback : DiffUtil.ItemCallback<Note>() {
    // This class extends DiffUtil.ItemCallback<Note>, which is used by the ListAdapter
    // to determine whether two items represent the same object and whether their contents are the same.

    // Checks if two items have the same ID. If they do, they are considered the same item.
    override fun areItemsTheSame(oldItem: Note, newItem: Note)
            = (oldItem.noteId == newItem.noteId)

    // Checks if the contents of two items are the same.
    // This comparison is used to check if the item's content has changed.
    override fun areContentsTheSame(oldItem: Note, newItem: Note) = (oldItem == newItem)
}

class ItemAdapter(val onItemClicked: (noteId: Long) -> Unit, val onDeleteClicked: (noteId: Long) -> Unit)
    : ListAdapter<Note, ItemAdapter.NoteItemViewHolder>(NoteDiffItemCallback()) {

    // Creates and returns a new NoteItemViewHolder for the RecyclerView.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : NoteItemViewHolder = NoteItemViewHolder.inflateFrom(parent)

    // Called by RecyclerView to display the data at the specified position.
    override fun onBindViewHolder(holder: NoteItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClicked, onDeleteClicked)
        // Binds the data to the ViewHolder.
    }

    // ViewHolder class for individual note items.
    class NoteItemViewHolder(val binding: ItemNoteBinding)
        : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun inflateFrom(parent: ViewGroup): NoteItemViewHolder {
                // Inflates the layout and creates a ViewHolder from it.
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemNoteBinding.inflate(layoutInflater, parent, false)
                return NoteItemViewHolder(binding)
            }
        }

        // Binds data to the ViewHolder.
        fun bind(item: Note, onItemClicked: (noteId: Long) -> Unit, onDeleteClicked: (noteId: Long) -> Unit) {
            binding.note = item
            // Sets an OnClickListener for the delete button.
            binding.deleteButton.setOnClickListener { onDeleteClicked(item.noteId) }
            // Sets an OnClickListener for the entire note item.
            binding.itemNoteRoot.setOnClickListener { onItemClicked(item.noteId) }
        }
    }
}