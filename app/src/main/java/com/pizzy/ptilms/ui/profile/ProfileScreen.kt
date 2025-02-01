package com.pizzy.ptilms.ui.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.pizzy.ptilms.ui.components.CustomOutlinedTextField
import com.pizzy.ptilms.viewmodel.ProfileViewModel
import com.pizzy.ptilms.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    userId: String,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = userId) {
        viewModel.loadUser(userId)
    }
    val userUiState by viewModel.userUiState.collectAsState()
    val formState by viewModel.formState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when (userUiState) {
                    is UiState.Loading -> {
                        CircularProgressIndicator()
                    }

                    is UiState.Success -> {
                        ProfileContent(
                            formState = formState,
                            onFormEvent = viewModel::onFormEvent,
                            onSubmit = viewModel::submitForm
                        )
                    }

                    is UiState.Error -> {
                        Text(
                            text = "Error: ${(userUiState as UiState.Error).message}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    is UiState.Empty -> {
                        Text(text = "No user found.")
                    }
                }
            }
        }
    )
}

@Composable
fun ProfileContent(
    formState: ProfileFormState,
    onFormEvent: (ProfileFormEvent) -> Unit,
    onSubmit: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onFormEvent(ProfileFormEvent.ProfilePictureChanged(uri))
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0E0E0))
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = formState.profilePictureUri ?: "https://via.placeholder.com/150",
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                placeholder = rememberAsyncImagePainter("https://via.placeholder.com/150"),
                error = rememberAsyncImagePainter("https://via.placeholder.com/150"),
                contentScale = ContentScale.Crop
            ) }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Tap image to change profile picture", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(16.dp))

        CustomOutlinedTextField(
            value = formState.username,
            onValueChange = { onFormEvent(ProfileFormEvent.UsernameChanged(it)) },
            label = "Username",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        CustomOutlinedTextField(
            value = formState.phoneNumber,
            onValueChange = { onFormEvent(ProfileFormEvent.PhoneNumberChanged(it)) },
            label = "Phone Number",
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        CustomOutlinedTextField(
            value = formState.dateOfBirth,
            onValueChange = { onFormEvent(ProfileFormEvent.DateOfBirthChanged(it)) },
            label = "Date of Birth (yyyy-MM-dd)",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        CustomOutlinedTextField(
            value = formState.sex,
            onValueChange = { onFormEvent(ProfileFormEvent.SexChanged(it)) },
            label = "Sex",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Display Role (Non-Editable)
        OutlinedTextField(
            value = formState.role,
            onValueChange = {},
            label = { Text("Role") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth(),
            enabled = !formState.isLoading
        ) {
            if (formState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text("Save Changes")
            }
        }
        if (formState.errorMessage != null) {
            Text(text = formState.errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}