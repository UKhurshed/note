package org.text.note.feature.detail


import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import org.text.note.data.model.Note
import org.text.note.data.repository.NotesRepository

class NoteDetailStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: NotesRepository
) {
    fun create(noteId: String): NoteDetailStore =
        object : NoteDetailStore, Store<NoteDetailStore.Intent, NoteDetailStore.State, NoteDetailStore.Label>
        by storeFactory.create(
            name = "NoteDetailStore",
            initialState = NoteDetailStore.State(),
            executorFactory = { ExecutorImpl(noteId) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Msg {
        data object Loading : Msg
        data class Loaded(val note: Note?) : Msg
    }

    private inner class ExecutorImpl(private val noteId: String) :
        CoroutineExecutor<NoteDetailStore.Intent, Nothing, NoteDetailStore.State, Msg, NoteDetailStore.Label>() {

        override fun executeIntent(intent: NoteDetailStore.Intent) {
            when (intent) {
                is NoteDetailStore.Intent.Load -> load()
                is NoteDetailStore.Intent.Delete -> delete()
            }
        }

        private fun load() {
            dispatch(Msg.Loading)
            dispatch(Msg.Loaded(repository.getById(noteId)))
        }

        private fun delete() {
            repository.delete(noteId)
            publish(NoteDetailStore.Label.Deleted)
        }
    }

    private object ReducerImpl : Reducer<NoteDetailStore.State, Msg> {
        override fun NoteDetailStore.State.reduce(msg: Msg): NoteDetailStore.State = when (msg) {
            is Msg.Loading -> copy(isLoading = true, notFound = false)
            is Msg.Loaded -> copy(
                note = msg.note,
                isLoading = false,
                notFound = msg.note == null
            )
        }
    }
}