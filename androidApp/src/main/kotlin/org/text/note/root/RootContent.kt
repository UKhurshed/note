package org.text.note.root
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import org.text.note.feature.detail.NoteDetailContent
import org.text.note.feature.edit.NoteEditContent
import org.text.note.feature.main.MainContent

@Composable
fun RootContent(component: RootComponent) {
    Children(
        stack = component.stack,
        animation = stackAnimation(slide())
    ) { child ->
        when (val instance = child.instance) {
            is RootComponent.Child.Main -> MainContent(instance.component)
            is RootComponent.Child.Detail -> NoteDetailContent(instance.component)
            is RootComponent.Child.Edit -> NoteEditContent(instance.component)
        }
    }
}