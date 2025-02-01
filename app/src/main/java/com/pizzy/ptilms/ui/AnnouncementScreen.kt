package com.pizzy.ptilms.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pizzy.ptilms.data.model.AnnouncementEntity
import com.pizzy.ptilms.navigation.Screen
import com.pizzy.ptilms.ui.components.AnnouncementItem
import com.pizzy.ptilms.viewmodel.AnnouncementViewModel
import com.pizzy.ptilms.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementScreen(
    announcementViewModel: AnnouncementViewModel,
    navController: NavHostController,
    courseId: String,
    userRole: String
) {
    val uiState by announcementViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = courseId) {
        announcementViewModel.getAnnouncementsByCourseId(courseId)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Announcements") }) },
        floatingActionButton = {
            if (userRole.equals("lecturer", ignoreCase = true)) {
                FloatingActionButton(onClick = { navController.navigate(Screen.AddAnnouncement.createRoute(courseId)) }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Announcement")
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is UiState.Success -> {
                    val announcements = (uiState as UiState.Success<List<AnnouncementEntity>>).data
                    if (announcements.isEmpty()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No announcements found",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(announcements) { announcement ->
                                AnnouncementItem(
                                    announcement = announcement,
                                    navController = navController
                                )
                            }
                        }
                    }
                }

                is UiState.Error -> {
                    val errorState = uiState as UiState.Error
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error: ${errorState.message}",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                is UiState.Empty -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No data available.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}