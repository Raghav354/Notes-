package com.example.notesapp.room

import androidx.lifecycle.LiveData

class NoteRepository(private val notesDao: NoteDao) {
    val allNotes: LiveData<MutableList<Note>> = notesDao.getAllNote()

    suspend fun insert(note: Note) {
        notesDao.insert(note)
    }

    suspend fun delete(note: Note) {
        notesDao.delete(note)
    }

    suspend fun update(note: Note) {
        notesDao.update(note)
    }

    suspend fun deleteAllNotes() {
        notesDao.deleteAllNote()
    }

}