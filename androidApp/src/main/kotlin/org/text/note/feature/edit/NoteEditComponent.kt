package org.text.note.feature.edit

import com.arkivanov.decompose.ComponentContext

class NoteEditComponent(
    componentContext: ComponentContext,
    private val noteId: String?,
    private val onDone: () -> Unit,
) : ComponentContext by componentContext {

    val editNoteId: String? = noteId

    fun onBack() = onDone()
}