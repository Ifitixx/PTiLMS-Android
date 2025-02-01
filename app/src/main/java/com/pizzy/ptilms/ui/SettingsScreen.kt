package com.pizzy.ptilms.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SettingsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        // Example setting item
        Text("Account Settings")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Notification Preferences")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Privacy and Security")

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { /* Navigate or perform setting updates here */ }) {
            Text("Save Changes")
        }
    }
}
