package org.text.note.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import org.text.note.data.repository.NotesRepository
import org.text.note.data.repository.NotesRepositoryImpl
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val appModule = module {
    single { Json { ignoreUnknownKeys = true; prettyPrint = false } }
    single<NotesRepository> { NotesRepositoryImpl(get(), get()) }
    single<StoreFactory> { DefaultStoreFactory() }
    factory { org.text.note.feature.main.MainStoreFactory(get(), get()) }
    factory { org.text.note.feature.edit.NoteEditStoreFactory(get(), get()) }
}