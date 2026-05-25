package org.text.note.feature.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import org.text.note.core.BaseStoreComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NoteDetailComponent(
    componentContext: ComponentContext,
    storeFactory: NoteDetailStoreFactory,
    val noteId: String,
    private val onEdit: (String) -> Unit,
    private val onBack: () -> Unit
) : BaseStoreComponent<NoteDetailStore.State, NoteDetailStore.Intent, NoteDetailStore.Label>(
    componentContext = componentContext,
    store = componentContext.instanceKeeper.getStore { storeFactory.create(noteId) }
) {
    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    init {
        // Перезагружаем при каждом возврате на экран
        lifecycle.doOnResume { accept(NoteDetailStore.Intent.Load) }

        labels
            .onEach { label ->
                if (label is NoteDetailStore.Label.Deleted) onBack()
            }
            .launchIn(scope)

        lifecycle.doOnDestroy { scope.cancel() }
    }

    fun onEditClicked() = onEdit(noteId)
    fun onDeleteClicked() = accept(NoteDetailStore.Intent.Delete)
    fun onBackClicked() = onBack()
}