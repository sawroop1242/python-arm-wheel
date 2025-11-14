package com.jupyter.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentUrl: String,
    currentToken: String,
    onSave: (String, String) -> Unit,
    onBack: () -> Unit
) {
    var url by remember { mutableStateOf(currentUrl) }
    var token by remember { mutableStateOf(currentToken) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onSave(url, token)
                            onBack()
                        }
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Save")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Jupyter Server Configuration",
                style = MaterialTheme.typography.headlineSmall
            )
            
            OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                label = { Text("Server URL") },
                placeholder = { Text("http://localhost:8888") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                singleLine = true
            )
            
            Text(
                text = "Examples:\n• http://localhost:8888\n• http://10.0.2.2:8888 (Android emulator)\n• http://192.168.1.100:8888 (Local network)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            OutlinedTextField(
                value = token,
                onValueChange = { token = it },
                label = { Text("Token (Optional)") },
                placeholder = { Text("Enter Jupyter token if required") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            Text(
                text = "Find your token in the Jupyter server terminal output or by running:\njupyter server list",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Quick Start",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "1. Start Jupyter on your computer:\n   jupyter notebook --ip=0.0.0.0\n\n2. Note the token from terminal output\n\n3. Enter the server URL and token above\n\n4. Tap Save and Connect",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
