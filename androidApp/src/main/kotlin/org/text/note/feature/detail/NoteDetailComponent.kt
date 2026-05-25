package org.text.note.feature.detail

import com.arkivanov.decompose.ComponentContext

class NoteDetailComponent(
    componentContext: ComponentContext,
    private val noteId: String,
    private val onEditNote: (String) -> Unit,
    private val onBackPressed: () -> Unit,
) : ComponentContext by componentContext {
    val detailNoteId: String = noteId
    fun onBack() = onBackPressed()
    fun onEdit(id: String) = onEditNote(id)
}