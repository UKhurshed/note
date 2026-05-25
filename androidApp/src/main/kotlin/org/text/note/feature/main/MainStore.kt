package org.text.note.feature.main

import com.arkivanov.mvikotlin.core.store.Store
import org.text.note.data.model.Note

interface MainStore : Store<MainStore.Intent, MainStore.State, MainStore.Label> {

    data class State(
        val notes: List<Note> = emptyList(),
        val isLoading: Boolean = false
    )

    sealed interface Intent {
        data object Load : Intent
        data class Delete(val id: String) : Intent
    }

    sealed interface Label {
        data class NoteDeleted(val title: String) : Label
    }
}