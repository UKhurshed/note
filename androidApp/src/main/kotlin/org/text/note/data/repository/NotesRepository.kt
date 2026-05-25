package org.text.note.data.repository

import org.text.note.data.model.Note

interface NotesRepository {
    fun getAll(): List<Note>
    fun getById(id: String): Note?
    fun save(note: Note)
    fun delete(id: String)
}