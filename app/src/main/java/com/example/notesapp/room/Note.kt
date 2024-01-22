package com.example.notesapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("note_table")
data class Note(
    val title: String,
    var description: String,
    var priority: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}