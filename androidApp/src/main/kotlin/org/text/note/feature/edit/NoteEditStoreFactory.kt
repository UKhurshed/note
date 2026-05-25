package org.text.note.feature.edit

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import org.text.note.data.model.Note
import org.text.note.data.repository.NotesRepository
import kotlin.random.Random
import kotlin.time.Clock

class NoteEditStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: NotesRepository
) {
    fun create(noteId: String?): NoteEditStore {
        val existing = noteId?.let { repository.getById(it) }
        val initial = NoteEditStore.State(
            title = existing?.title.orEmpty(),
            content = existing?.content.orEmpty(),
            isNew = existing == null
        )

        return object : NoteEditStore, Store<NoteEditStore.Intent, NoteEditStore.State, NoteEditStore.Label>
        by storeFactory.create(
            name = "NoteEditStore",
            initialState = initial,
            executorFactory = { ExecutorImpl(noteId) },
            reducer = ReducerImpl
        ) {}
    }

    private sealed interface Msg {
        data class TitleUpdated(val value: String) : Msg
        data class ContentUpdated(val value: String) : Msg
    }

    private inner class ExecutorImpl(private val noteId: String?) :
        CoroutineExecutor<NoteEditStore.Intent, Nothing, NoteEditStore.State, Msg, NoteEditStore.Label>() {

        override fun executeIntent(intent: NoteEditStore.Intent) {
            when (intent) {
                is NoteEditStore.Intent.TitleChanged -> dispatch(Msg.TitleUpdated(intent.value))
                is NoteEditStore.Intent.ContentChanged -> dispatch(Msg.ContentUpdated(intent.value))
                is NoteEditStore.Intent.Save -> save()
                is NoteEditStore.Intent.Cancel -> publish(NoteEditStore.Label.Cancelled)
            }
        }

        private fun save() {
            val current = state()
            if (!current.canSave) {
                publish(NoteEditStore.Label.ValidationFailed)
                return
            }
            val now = Clock.System.now().toEpochMilliseconds()
            val existing = noteId?.let { repository.getById(it) }
            val note = Note(
                id = existing?.id ?: generateId(),
                title = current.title.trim(),
                content = current.content.trim(),
                createdAt = existing?.createdAt ?: now,
                updatedAt = now
            )
            repository.save(note)
            publish(NoteEditStore.Label.Saved)
        }

        private fun generateId(): String =
            "note-${Clock.System.now().toEpochMilliseconds()}-${Random.nextInt(1000, 9999)}"
    }

    private object ReducerImpl : Reducer<NoteEditStore.State, Msg> {
        override fun NoteEditStore.State.reduce(msg: Msg): NoteEditStore.State = when (msg) {
            is Msg.TitleUpdated -> copy(title = msg.value)
            is Msg.ContentUpdated -> copy(content = msg.value)
        }
    }
}