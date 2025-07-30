package com.test.snotes.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.test.snotes.dataModel.Note
import com.test.snotes.utils.LoaderState
import kotlinx.coroutines.tasks.await

class NotesRepository {
    private val db = FirebaseDatabase.getInstance().reference
    private val userId get() = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    suspend fun getNotes(): List<Note> {
        LoaderState.show()
        return try {
            val snapshot = db.child("notes").child(userId).get().await()
            val notes = mutableListOf<Note>()
            for (child in snapshot.children) {
                child.getValue(Note::class.java)?.let { notes.add(it) }
            }
            notes
        } finally {
            LoaderState.hide()
        }
    }


    suspend fun addNote(note: Note): String {
        LoaderState.show()
        return try {
            val noteId = db.child("notes").child(userId).push().key ?: return ""
            val newNote = note.copy(id = noteId)
            db.child("notes").child(userId).child(noteId).setValue(newNote).await()
            noteId
        } finally {
            LoaderState.hide()
        }
    }

    suspend fun updateNote(note: Note) {
        LoaderState.show()
        try {
            db.child("notes").child(userId).child(note.id).setValue(note).await()
        } finally {
            LoaderState.hide()
        }

    }

    suspend fun deleteNote(noteId: String) {
        LoaderState.show()
        try {
            db.child("notes").child(userId).child(noteId).removeValue().await()
        } finally {
            LoaderState.hide()
        }
    }
}
