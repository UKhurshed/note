package org.text.note.feature.detail

import com.arkivanov.mvikotlin.core.store.Store
import org.text.note.data.model.Note

interface NoteDetailStore : Store<NoteDetailStore.Intent, NoteDetailStore.State, NoteDetailStore.Label> {

    data class State(
        val note: Note? = null,
        val isLoading: Boolean = false,
        val notFound: Boolean = false
    )

    sealed interface Intent {
        data object Load : Intent
        data object Delete : Intent
    }

    sealed interface Label {
        data object Deleted : Label
    }
}