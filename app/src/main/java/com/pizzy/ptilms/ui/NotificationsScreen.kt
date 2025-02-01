package com.pizzy.ptilms.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NotificationsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Notifications", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        // Sample notification items
        Text("Assignment due in 2 days")
        Spacer(modifier = Modifier.height(16.dp))
        Text("New announcement from your course")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Grade update in your profile")

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { /* Handle notification actions here */ }) {
            Text("Mark All as Read")
        }
    }
}
