package org.text.note.feature.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import org.text.note.core.BaseStoreComponent

class MainComponent(
    componentContext: ComponentContext,
    storeFactory: MainStoreFactory,
    private val onAddNote: () -> Unit,
    private val onOpenNote: (String) -> Unit
) : BaseStoreComponent<MainStore.State, MainStore.Intent, MainStore.Label>(
    componentContext = componentContext,
    store = componentContext.instanceKeeper.getStore { storeFactory.create() }
) {
    init {
        accept(MainStore.Intent.Load)
    }
    fun onAddClicked() = onAddNote()
    fun onNoteClicked(id: String) = onOpenNote(id)
    fun onDeleteClicked(id: String) = accept(MainStore.Intent.Delete(id))
    fun reload() = accept(MainStore.Intent.Load)
}