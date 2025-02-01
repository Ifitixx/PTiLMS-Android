package com.pizzy.ptilms.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pizzy.ptilms.data.model.AssignmentEntity
import com.pizzy.ptilms.viewmodel.AssignmentViewModel
import com.pizzy.ptilms.viewmodel.UiState
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentDetailScreen(
    navController: NavController,
    assignmentId: String,
    viewModel: AssignmentViewModel = hiltViewModel()
) {
    val uiState by viewModel.assignmentDetailUiState.collectAsState()

    LaunchedEffect(key1 = assignmentId) {
        viewModel.getAssignmentById(assignmentId.toInt())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Assignment Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            when (uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                is UiState.Success -> {
                    val assignment = (uiState as UiState.Success<AssignmentEntity>).data
                    Column {
                        Text(
                            text = assignment.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = assignment.description,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        val formattedDate = SimpleDateFormat(
                            "dd MMM yyyy, HH:mm",
                            Locale.getDefault()
                        ).format(assignment.dueDate)
                        Text(
                            text = "Due Date: $formattedDate",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                is UiState.Error -> {
                    Text(
                        text = "Error: ${(uiState as UiState.Error).message}",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is UiState.Empty -> {
                    Text(text = "No data available.")
                }
            }
        }
    }
}