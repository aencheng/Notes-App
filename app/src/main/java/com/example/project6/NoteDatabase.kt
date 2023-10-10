package com.example.project6

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    // Abstract property for accessing the DAO (Data Access Object) for notes.
    abstract val notesDao: NotesDao
    companion object {
        @Volatile
        // Volatile ensures that INSTANCE is always up-to-date and the same to all execution threads.
        private var INSTANCE: NoteDatabase? = null

        // Marks the function as requiring an experimental coroutine API.
        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(context: Context): NoteDatabase{

            // Synchronized ensures that only one thread can execute this block at a time.
            synchronized(this){
                var instance = INSTANCE

                // If the database instance has not been created yet.
                if(instance == null){

                    // Build a new database instance using Room's databaseBuilder.
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NoteDatabase::class.java,
                        "notes_database").build()

                    // Set the INSTANCE variable to the newly created instance.
                    INSTANCE = instance
                }
                // Return the database instance.
                return instance
            }
        }
    }
}
