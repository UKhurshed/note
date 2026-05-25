package org.text.note

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import com.arkivanov.decompose.defaultComponentContext
import org.koin.android.ext.android.inject
import org.text.note.feature.detail.NoteDetailStoreFactory
import org.text.note.feature.edit.NoteEditStoreFactory
import org.text.note.feature.main.MainStoreFactory
import org.text.note.root.RootComponent
import org.text.note.root.RootContent

class MainActivity : ComponentActivity() {
    private val mainStoreFactory: MainStoreFactory by inject()
    private val editStoreFactory: NoteEditStoreFactory by inject()

    private val detailStoreFactory: NoteDetailStoreFactory by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val root = RootComponent(
            defaultComponentContext(),
            mainStoreFactory = mainStoreFactory,
            editStoreFactory = editStoreFactory,
            detailStoreFactory = detailStoreFactory,
        )
        setContent {
            MaterialTheme {
                RootContent(root)
            }
        }
    }
}
