package org.text.note.feature.edit

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import org.text.note.core.BaseStoreComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NoteEditComponent(
    componentContext: ComponentContext,
    storeFactory: NoteEditStoreFactory,
    noteId: String?,
    private val onDone: () -> Unit
) : BaseStoreComponent<NoteEditStore.State, NoteEditStore.Intent, NoteEditStore.Label>(
    componentContext = componentContext,
    store = componentContext.instanceKeeper.getStore { storeFactory.create(noteId) }
) {
    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    init {
        labels
            .onEach { label ->
                when (label) {
                    NoteEditStore.Label.Saved, NoteEditStore.Label.Cancelled -> onDone()
                    NoteEditStore.Label.ValidationFailed -> Unit // обработаем в UI
                }
            }
            .launchIn(scope)

        lifecycle.doOnDestroy { scope.cancel() }
    }

    fun onTitleChanged(value: String) = accept(NoteEditStore.Intent.TitleChanged(value))
    fun onContentChanged(value: String) = accept(NoteEditStore.Intent.ContentChanged(value))
    fun onSaveClicked() = accept(NoteEditStore.Intent.Save)
    fun onCancelClicked() = accept(NoteEditStore.Intent.Cancel)
}