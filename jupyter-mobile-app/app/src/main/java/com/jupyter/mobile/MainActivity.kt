package com.jupyter.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jupyter.mobile.ui.screens.MainScreen
import com.jupyter.mobile.ui.screens.SettingsScreen
import com.jupyter.mobile.ui.theme.JupyterMobileTheme
import com.jupyter.mobile.viewmodel.JupyterViewModel

class MainActivity : ComponentActivity() {
    
    private val viewModel: JupyterViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        setContent {
            JupyterMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    JupyterApp(viewModel)
                }
            }
        }
    }
    
    private fun toggleFullscreen(isFullscreen: Boolean) {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = 
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        
        if (isFullscreen) {
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        } else {
            windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
        }
    }
}

@Composable
fun JupyterApp(viewModel: JupyterViewModel) {
    val navController = rememberNavController()
    val connectionState by viewModel.connectionState.collectAsState()
    val currentUrl by viewModel.currentUrl.collectAsState()
    val serverUrl by viewModel.serverUrl.collectAsState()
    val token by viewModel.token.collectAsState()
    val isFullscreen by viewModel.isFullscreen.collectAsState()
    
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(
                connectionState = connectionState,
                currentUrl = currentUrl,
                isFullscreen = isFullscreen,
                onConnect = { viewModel.connect() },
                onDisconnect = { viewModel.disconnect() },
                onSettingsClick = { navController.navigate("settings") },
                onToggleFullscreen = { viewModel.toggleFullscreen() },
                onConnectionStateChanged = { state -> viewModel.setConnectionState(state) },
                onLabClick = {
                    viewModel.setConnectionState(com.jupyter.mobile.data.ConnectionState.Connecting)
                    // Update URL to Lab interface
                    val labUrl = viewModel.getLabUrl()
                    viewModel.connect()
                },
                onNotebookClick = {
                    viewModel.setConnectionState(com.jupyter.mobile.data.ConnectionState.Connecting)
                    // Update URL to Notebook interface
                    val notebookUrl = viewModel.getNotebookUrl()
                    viewModel.connect()
                }
            )
        }
        
        composable("settings") {
            SettingsScreen(
                currentUrl = serverUrl,
                currentToken = token,
                onSave = { url, newToken ->
                    viewModel.updateServerUrl(url)
                    viewModel.updateToken(newToken)
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
