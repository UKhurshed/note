package org.text.note

import android.app.Application
import org.text.note.di.appModule
import org.text.note.di.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NotesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NotesApplication)
            modules(appModule, platformModule)
        }
    }
}