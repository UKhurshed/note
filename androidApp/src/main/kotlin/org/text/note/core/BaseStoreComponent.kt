package org.text.note.core

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseStoreComponent<State : Any, Intent : Any, Label : Any>(
    componentContext: ComponentContext,
    private val store: Store<Intent, State, Label>
) : ComponentContext by componentContext {
    init {
        lifecycle.doOnDestroy(store::dispose)
    }

    val state: StateFlow<State> = store.stateFlow
    val labels: Flow<Label> = store.labels

    fun accept(intent: Intent) {
        store.accept(intent)
    }
}