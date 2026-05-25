package org.text.note.feature.main

import com.arkivanov.decompose.ComponentContext

class MainComponent(
    componentContext: ComponentContext,
    private val onAddNote: () -> Unit,
    private val onOpenNote: (String) -> Unit
) : ComponentContext by componentContext {

    fun onAddClicked() = onAddNote()
    fun onNoteClicked(id: String) = onOpenNote(id)
}