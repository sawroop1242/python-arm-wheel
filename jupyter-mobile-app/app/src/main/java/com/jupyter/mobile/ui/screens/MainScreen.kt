package com.jupyter.mobile.ui.screens

import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jupyter.mobile.data.ConnectionState
import com.jupyter.mobile.ui.components.JupyterWebView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    connectionState: ConnectionState,
    currentUrl: String,
    isFullscreen: Boolean,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit,
    onSettingsClick: () -> Unit,
    onToggleFullscreen: () -> Unit,
    onConnectionStateChanged: (ConnectionState) -> Unit,
    onLabClick: () -> Unit,
    onNotebookClick: () -> Unit
) {
    var webView by remember { mutableStateOf<WebView?>(null) }
    var canGoBack by remember { mutableStateOf(false) }
    var canGoForward by remember { mutableStateOf(false) }
    
    BackHandler(enabled = canGoBack) {
        webView?.goBack()
    }
    
    Scaffold(
        topBar = {
            if (!isFullscreen) {
                TopAppBar(
                    title = { 
                        Column {
                            Text("Jupyter Mobile")
                            when (connectionState) {
                                is ConnectionState.Connected -> {
                                    Text(
                                        "Connected",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                is ConnectionState.Connecting -> {
                                    Text(
                                        "Connecting...",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                is ConnectionState.Error -> {
                                    Text(
                                        "Error",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                                else -> {}
                            }
                        }
                    },
                    actions = {
                        IconButton(onClick = onSettingsClick) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                        IconButton(onClick = onToggleFullscreen) {
                            Icon(Icons.Default.Fullscreen, contentDescription = "Fullscreen")
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (!isFullscreen && connectionState is ConnectionState.Connected) {
                BottomAppBar(
                    actions = {
                        IconButton(
                            onClick = { webView?.goBack() },
                            enabled = canGoBack
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                        IconButton(
                            onClick = { webView?.goForward() },
                            enabled = canGoForward
                        ) {
                            Icon(Icons.Default.ArrowForward, contentDescription = "Forward")
                        }
                        IconButton(onClick = { webView?.reload() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                        }
                        IconButton(onClick = onLabClick) {
                            Icon(Icons.Default.Science, contentDescription = "JupyterLab")
                        }
                        IconButton(onClick = onNotebookClick) {
                            Icon(Icons.Default.Description, contentDescription = "Notebook")
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (isFullscreen) PaddingValues(0.dp) else paddingValues)
        ) {
            when (connectionState) {
                is ConnectionState.Disconnected -> {
                    DisconnectedView(onConnect = onConnect)
                }
                is ConnectionState.Connecting -> {
                    LoadingView()
                }
                is ConnectionState.Connected -> {
                    JupyterWebView(
                        url = currentUrl,
                        modifier = Modifier.fillMaxSize(),
                        onConnectionStateChanged = onConnectionStateChanged,
                        onWebViewCreated = { view ->
                            webView = view
                        }
                    )
                    
                    // Update navigation state
                    LaunchedEffect(webView) {
                        webView?.let { view ->
                            canGoBack = view.canGoBack()
                            canGoForward = view.canGoForward()
                        }
                    }
                }
                is ConnectionState.Error -> {
                    ErrorView(
                        message = connectionState.message,
                        onRetry = onConnect,
                        onSettings = onSettingsClick
                    )
                }
            }
            
            // Fullscreen exit button
            if (isFullscreen) {
                IconButton(
                    onClick = onToggleFullscreen,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Icon(
                            Icons.Default.FullscreenExit,
                            contentDescription = "Exit Fullscreen",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DisconnectedView(onConnect: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.CloudOff,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Not Connected",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Connect to your Jupyter server to get started",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onConnect) {
            Icon(Icons.Default.PlayArrow, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Connect")
        }
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator()
            Text("Connecting to Jupyter...")
        }
    }
}

@Composable
fun ErrorView(
    message: String,
    onRetry: () -> Unit,
    onSettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Error,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Connection Failed",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(onClick = onSettings) {
                Icon(Icons.Default.Settings, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Settings")
            }
            Button(onClick = onRetry) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Retry")
            }
        }
    }
}
