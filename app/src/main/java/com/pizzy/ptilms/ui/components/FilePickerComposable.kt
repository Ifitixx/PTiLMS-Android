package com.pizzy.ptilms.ui.components

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.pizzy.ptilms.viewmodel.CourseDetailsViewModel

@Composable
fun FilePickerComposable(
    onFileSelected: (Uri) -> Unit,
    fileType: String,
    courseDetailsViewModel: CourseDetailsViewModel = hiltViewModel()
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            if (uri != null) {
                courseDetailsViewModel.handleFileSelectionResult(
                    requestCode = courseDetailsViewModel.getRequestCodeForFileType(fileType),
                    data = Intent().apply { data = uri }
                )
                onFileSelected(uri)
            }
        }
    )

    Button(onClick = {
        launcher.launch(
            when (fileType) {
                "pdf" -> arrayOf("application/pdf")
                "image" -> arrayOf("image/*")
                "video" -> arrayOf("video/*")
                else -> arrayOf("*/*")
            }
        )
    }) {
        Text("Select $fileType")
    }
}
