package com.meli.test.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.meli.test.data.repository.ProductRepositoryImpl
import com.meli.test.domain.repository.ProductRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val Context.encryptedDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "encrypted_auth_prefs"
)

val dataModule = module {
    single<DataStore<Preferences>> { androidContext().encryptedDataStore }

    single<ProductRepository> { ProductRepositoryImpl(get()) }
}