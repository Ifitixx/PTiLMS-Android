package com.pizzy.ptilms.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pizzy.ptilms.viewmodel.AnnouncementViewModel
import com.pizzy.ptilms.viewmodel.UiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAnnouncementScreen(
    navController: NavController,
    courseId: String,
    viewModel: AnnouncementViewModel = hiltViewModel()
) {
    var announcementTitle by remember { mutableStateOf("") }
    var announcementContent by remember { mutableStateOf("") }
    val addAnnouncementUiState by viewModel.addAnnouncementUiState.collectAsState()
    val currentUserUiState by viewModel.currentUser.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = currentUserUiState) {
        if (currentUserUiState is UiState.Error) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = (currentUserUiState as UiState.Error).message,
                    withDismissAction = true
                )
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Announcement") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (currentUserUiState is UiState.Loading) {
                CircularProgressIndicator()
            }
            OutlinedTextField(
                value = announcementTitle,
                onValueChange = { announcementTitle = it },
                label = { Text("Announcement Title") },
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = announcementContent,
                onValueChange = { announcementContent = it },
                label = { Text("Announcement Content") },
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (announcementTitle.isNotBlank() && announcementContent.isNotBlank()) {
                        viewModel.addAnnouncement(
                            title = announcementTitle,
                            content = announcementContent,
                            courseId = courseId
                        )
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Please fill all the fields",
                                withDismissAction = true
                            )
                        }
                    }
                },
                enabled = currentUserUiState !is UiState.Loading && announcementContent.isNotBlank() && announcementTitle.isNotBlank()
            ) {
                Text("Save Announcement")
            }

            when (val state = addAnnouncementUiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator()
                }

                is UiState.Empty -> {
                    LaunchedEffect(key1 = addAnnouncementUiState) {
                        Toast.makeText(context, "Announcement added", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                }

                is UiState.Error -> {
                    LaunchedEffect(key1 = state.message) {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = state.message,
                                withDismissAction = true
                            )
                        }
                    }
                }

                else -> {}
            }
        }
    }
}