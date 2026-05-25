package org.text.note.di

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val platformModule = module {
    single<Settings> {
        val prefs = androidContext()
            .getSharedPreferences("notes_prefs", Context.MODE_PRIVATE)
        SharedPreferencesSettings(prefs)
    }
}