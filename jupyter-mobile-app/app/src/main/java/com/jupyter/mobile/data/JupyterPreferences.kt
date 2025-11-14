package com.jupyter.mobile.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "jupyter_settings")

class JupyterPreferences(private val context: Context) {
    
    companion object {
        private val SERVER_URL_KEY = stringPreferencesKey("server_url")
        private val TOKEN_KEY = stringPreferencesKey("token")
        const val DEFAULT_URL = "http://localhost:8888"
    }
    
    val serverUrl: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[SERVER_URL_KEY] ?: DEFAULT_URL
    }
    
    val token: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[TOKEN_KEY] ?: ""
    }
    
    suspend fun saveServerUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[SERVER_URL_KEY] = url
        }
    }
    
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }
}
