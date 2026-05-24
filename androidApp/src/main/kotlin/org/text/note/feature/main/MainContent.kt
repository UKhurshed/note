package org.text.note.feature.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(component: MainComponent) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Заметки") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = component::onAddClicked) {
                Icon(androidx.compose.material.icons.Icons.Default.Add, null)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Главный экран")
            Button(onClick = { component.onNoteClicked("test-id") }) {
                Text("Открыть детали")
            }
        }
    }
}