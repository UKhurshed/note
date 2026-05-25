package org.text.note.feature.edit

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditContent(component: NoteEditComponent) {
    val state by component.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        component.labels.collect { label ->
            if (label is NoteEditStore.Label.ValidationFailed) {
                snackbarHostState.showSnackbar("Заголовок не может быть пустым")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isNew) "Новая заметка" else "Редактирование") },
                navigationIcon = {
                    IconButton(onClick = component::onCancelClicked) {
                        Icon(Icons.Default.Close, contentDescription = "Отмена")
                    }
                },
                actions = {
                    TextButton(
                        onClick = component::onSaveClicked,
                        enabled = state.canSave
                    ) { Text("Сохранить") }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = component::onTitleChanged,
                label = { Text("Заголовок") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.content,
                onValueChange = component::onContentChanged,
                label = { Text("Текст заметки") },
                modifier = Modifier.fillMaxWidth().weight(1f)
            )
        }
    }
}