package org.text.note.feature.edit

import com.arkivanov.mvikotlin.core.store.Store

interface NoteEditStore : Store<NoteEditStore.Intent, NoteEditStore.State, NoteEditStore.Label> {

    data class State(
        val title: String = "",
        val content: String = "",
        val isLoading: Boolean = false,
        val isNew: Boolean = true
    ) {
        val canSave: Boolean get() = title.isNotBlank()
    }

    sealed interface Intent {
        data class TitleChanged(val value: String) : Intent
        data class ContentChanged(val value: String) : Intent
        data object Save : Intent
        data object Cancel : Intent
    }

    sealed interface Label {
        data object Saved : Label
        data object Cancelled : Label
        data object ValidationFailed : Label
    }
}