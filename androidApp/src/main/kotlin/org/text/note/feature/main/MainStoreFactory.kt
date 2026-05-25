package org.text.note.feature.main

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import org.text.note.data.model.Note
import org.text.note.data.repository.NotesRepository

class MainStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: NotesRepository
) {
    fun create(): MainStore =
        object : MainStore, Store<MainStore.Intent, MainStore.State, MainStore.Label>
        by storeFactory.create(
            name = "MainStore",
            initialState = MainStore.State(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Msg {
        data object Loading : Msg
        data class NotesLoaded(val notes: List<Note>) : Msg
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<MainStore.Intent, Nothing, MainStore.State, Msg, MainStore.Label>() {

        override fun executeIntent(intent: MainStore.Intent) {
            when (intent) {
                is MainStore.Intent.Load -> loadNotes()
                is MainStore.Intent.Delete -> deleteNote(intent.id)
            }
        }

        private fun loadNotes() {
            dispatch(Msg.Loading)
            val notes = repository.getAll().sortedByDescending { it.updatedAt }
            dispatch(Msg.NotesLoaded(notes))
        }

        private fun deleteNote(id: String) {
            val note = repository.getById(id) ?: return
            repository.delete(id)
            publish(MainStore.Label.NoteDeleted(note.title))
            loadNotes()
        }
    }

    private object ReducerImpl : Reducer<MainStore.State, Msg> {
        override fun MainStore.State.reduce(msg: Msg): MainStore.State = when (msg) {
            is Msg.Loading -> copy(isLoading = true)
            is Msg.NotesLoaded -> copy(notes = msg.notes, isLoading = false)
        }
    }
}