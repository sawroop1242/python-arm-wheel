package com.jupyter.mobile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jupyter.mobile.data.ConnectionState
import com.jupyter.mobile.data.JupyterPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JupyterViewModel(application: Application) : AndroidViewModel(application) {
    
    private val preferences = JupyterPreferences(application)
    
    private val _serverUrl = MutableStateFlow(JupyterPreferences.DEFAULT_URL)
    val serverUrl: StateFlow<String> = _serverUrl.asStateFlow()
    
    private val _token = MutableStateFlow("")
    val token: StateFlow<String> = _token.asStateFlow()
    
    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()
    
    private val _currentUrl = MutableStateFlow("")
    val currentUrl: StateFlow<String> = _currentUrl.asStateFlow()
    
    private val _isFullscreen = MutableStateFlow(false)
    val isFullscreen: StateFlow<Boolean> = _isFullscreen.asStateFlow()
    
    init {
        loadPreferences()
    }
    
    private fun loadPreferences() {
        viewModelScope.launch {
            preferences.serverUrl.collect { url ->
                _serverUrl.value = url
            }
        }
        viewModelScope.launch {
            preferences.token.collect { token ->
                _token.value = token
            }
        }
    }
    
    fun updateServerUrl(url: String) {
        viewModelScope.launch {
            preferences.saveServerUrl(url)
            _serverUrl.value = url
        }
    }
    
    fun updateToken(token: String) {
        viewModelScope.launch {
            preferences.saveToken(token)
            _token.value = token
        }
    }
    
    fun connect() {
        _connectionState.value = ConnectionState.Connecting
        val url = buildJupyterUrl()
        _currentUrl.value = url
        // Connection state will be updated by WebView callbacks
    }
    
    fun disconnect() {
        _connectionState.value = ConnectionState.Disconnected
        _currentUrl.value = ""
    }
    
    fun setConnectionState(state: ConnectionState) {
        _connectionState.value = state
    }
    
    fun toggleFullscreen() {
        _isFullscreen.value = !_isFullscreen.value
    }
    
    private fun buildJupyterUrl(): String {
        val baseUrl = _serverUrl.value.trimEnd('/')
        val tokenParam = if (_token.value.isNotEmpty()) "?token=${_token.value}" else ""
        return "$baseUrl$tokenParam"
    }
    
    fun getLabUrl(): String {
        val baseUrl = _serverUrl.value.trimEnd('/')
        val tokenParam = if (_token.value.isNotEmpty()) "?token=${_token.value}" else ""
        return "$baseUrl/lab$tokenParam"
    }
    
    fun getNotebookUrl(): String {
        val baseUrl = _serverUrl.value.trimEnd('/')
        val tokenParam = if (_token.value.isNotEmpty()) "?token=${_token.value}" else ""
        return "$baseUrl/tree$tokenParam"
    }
}
