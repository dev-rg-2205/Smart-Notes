package com.test.snotes.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.snotes.dataModel.Note
import com.test.snotes.repository.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotesViewModel : ViewModel() {
    private val repo = NotesRepository()

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    fun loadNotes() {
        viewModelScope.launch {
            _notes.value = repo.getNotes()
        }
    }

    fun addNote(note: Note): String {
        var noteId = ""
        viewModelScope.launch {
            noteId = repo.addNote(note)
            loadNotes()
        }
        return noteId
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repo.updateNote(note)
            loadNotes()
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            repo.deleteNote(noteId)
            loadNotes()
        }
    }
}
