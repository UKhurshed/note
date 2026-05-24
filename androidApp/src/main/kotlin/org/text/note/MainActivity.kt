package org.text.note

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import com.arkivanov.decompose.defaultComponentContext
import org.text.note.root.RootComponent
import org.text.note.root.RootContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val root = RootComponent(defaultComponentContext())
        setContent {
            MaterialTheme {
                RootContent(root)
            }
        }
    }
}
