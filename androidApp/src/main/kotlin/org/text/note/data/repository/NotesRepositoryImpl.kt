package org.text.note.data.repository

import kotlinx.serialization.json.Json
import org.text.note.data.model.Note
import com.russhwolf.settings.Settings
import kotlinx.serialization.encodeToString

class NotesRepositoryImpl(
    private val settings: Settings,
    private val json: Json
): NotesRepository {
    private val key = "notes_list"

    override fun getAll(): List<Note> {
        val raw = settings.getStringOrNull(key) ?: return emptyList()
        return runCatching { json.decodeFromString<List<Note>>(raw) }
            .getOrDefault(emptyList())
    }

    override fun getById(id: String): Note? = getAll().find { it.id == id }

    override fun save(note: Note) {
        val all = getAll().toMutableList()
        val idx = all.indexOfFirst { it.id == note.id }
        if (idx >= 0) all[idx] = note else all.add(note)
        settings.putString(key, json.encodeToString(all))
    }

    override fun delete(id: String) {
        val all = getAll().filter { it.id != id }
        settings.putString(key, json.encodeToString(all))
    }
}