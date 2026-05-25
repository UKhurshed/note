package org.text.note.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import org.text.note.feature.detail.NoteDetailComponent
import org.text.note.feature.edit.NoteEditComponent
import org.text.note.feature.main.MainComponent
import kotlinx.serialization.Serializable
import org.text.note.feature.main.MainStoreFactory

class RootComponent(
    componentContext: ComponentContext,
    private val mainStoreFactory: MainStoreFactory
) : ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    val stack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Main,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(config: Config, ctx: ComponentContext): Child =
        when (config) {
            is Config.Main -> Child.Main(
                MainComponent(
                    componentContext = ctx,
                    storeFactory = mainStoreFactory,
                    onAddNote = { navigation.pushNew(Config.Edit(noteId = null)) },
                    onOpenNote = { id -> navigation.pushNew(Config.Detail(id)) }
                )
            )
            is Config.Detail -> Child.Detail(
                NoteDetailComponent(
                    componentContext = ctx,
                    noteId = config.noteId,
                    onEditNote = { id -> navigation.pushNew(Config.Edit(id)) },
                    onBackPressed = { navigation.pop() }
                )
            )
            is Config.Edit -> Child.Edit(
                NoteEditComponent(
                    componentContext = ctx,
                    noteId = config.noteId,
                    onDone = { navigation.pop() }
                )
            )
        }

    @Serializable
    sealed interface Config {
        @Serializable data object Main : Config
        @Serializable data class Detail(val noteId: String) : Config
        @Serializable data class Edit(val noteId: String?) : Config
    }

    sealed interface Child {
        class Main(val component: MainComponent) : Child
        class Detail(val component: NoteDetailComponent) : Child
        class Edit(val component: NoteEditComponent) : Child
    }
}