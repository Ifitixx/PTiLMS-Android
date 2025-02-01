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
import com.pizzy.ptilms.ui.components.MyDatePickerDialog
import com.pizzy.ptilms.viewmodel.AssignmentViewModel
import com.pizzy.ptilms.viewmodel.UiState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssignmentScreen(
    navController: NavController,
    courseId: String,
    viewModel: AssignmentViewModel = hiltViewModel()
) {
    var assignmentTitle by remember { mutableStateOf("") }
    var assignmentDescription by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var dueDate by remember { mutableStateOf(Date()) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val addAssignmentUiState by viewModel.addAssignmentUiState.collectAsState(initial = UiState.Empty)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Assignment") },
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
            OutlinedTextField(
                value = assignmentTitle,
                onValueChange = { assignmentTitle = it },
                label = { Text("Assignment Title") },
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = assignmentDescription,
                onValueChange = { assignmentDescription = it },
                label = { Text("Assignment Description") },
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Button(onClick = { showDatePicker = true }) {
                val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(dueDate)
                Text("Due Date: $formattedDate")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (assignmentTitle.isNotBlank() && assignmentDescription.isNotBlank()) {
                        viewModel.addAssignment(
                            assignmentTitle,
                            assignmentDescription,
                            dueDate,
                            courseId.toInt()
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
                enabled = assignmentTitle.isNotBlank() && assignmentDescription.isNotBlank()
            ) {
                Text("Save Assignment")
            }

            when (val state = addAssignmentUiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator()
                }

                is UiState.Empty -> {
                    LaunchedEffect(key1 = addAssignmentUiState) {
                        Toast.makeText(context, "Assignment added", Toast.LENGTH_SHORT).show()
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
    if (showDatePicker) {
        MyDatePickerDialog(
            onDateSelected = { selectedDate ->
                dueDate = selectedDate
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}