package com.pizzy.ptilms

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class PermissionsHandler(private val activity: ComponentActivity) {

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    fun register() {
        permissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionsMap ->
            handlePermissionResults(permissionsMap)
        }
    }

    fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
                )
            )
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }
    }

    private fun handlePermissionResults(permissionsMap: Map<String, Boolean>) {
        for ((permission, isGranted) in permissionsMap) {
            if (!isGranted) {
                val message = when (permission) {
                    Manifest.permission.READ_EXTERNAL_STORAGE ->
                        "Read external storage permission denied"
                    Manifest.permission.WRITE_EXTERNAL_STORAGE ->
                        "Write external storage permission denied"
                    Manifest.permission.READ_MEDIA_IMAGES ->
                        "Read media images permission denied"
                    else -> "Permission denied for $permission"
                }
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}