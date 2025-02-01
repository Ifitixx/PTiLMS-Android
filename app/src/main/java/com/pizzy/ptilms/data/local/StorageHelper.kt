package com.pizzy.ptilms.data.local

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.pizzy.ptilms.utils.StorageFullException
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageHelper @Inject constructor(@ApplicationContext private val context: Context) {

    private fun getCourseContentDir(courseId: Int, contentType: String): File {
        val filesDir = context.filesDir
        val courseContentDir = File(filesDir, "course_content/$courseId")
        val contentTypeDir = File(courseContentDir, contentType)
        if (!contentTypeDir.exists()) {
            contentTypeDir.mkdirs()
        }
        return contentTypeDir
    }

    @Composable
    fun DisposableFile(courseId: Int, contentType: String, fileName: String) {
        val file = remember { File(getCourseContentDir(courseId, contentType), fileName) }

        DisposableEffect(Unit) {
            onDispose {
                releaseFile(file)
            }
        }
    }

    @Throws(StorageFullException::class, FileNotFoundException::class)
    fun saveFile(courseId: Int, contentType: String, fileName: String, content: ByteArray): Uri? {
        val file = File(getCourseContentDir(courseId, contentType), fileName)
        return try {
            file.outputStream().use { it.write(content) }
            Uri.fromFile(file)
        } catch (e: IOException) {
            Log.e("StorageHelper", "Failed to save file: ${e.message}")
            if (e is FileNotFoundException) {
                throw e // Re-throw FileNotFoundException
            } else {
                throw StorageFullException("Failed to save file: ${e.message}")
            }
        }
    }

    @Throws(FileNotFoundException::class)
    fun getFile(filePath: String): File {
        val file = File(filePath)
        if (!file.exists()) {
            throw FileNotFoundException("File not found: $filePath")
        }
        return file
    }

    fun releaseFile(file: File) {
        if (file.exists()) {
            // Perform any necessary cleanup for the specific file type
            // For example, if it's a video file, release the media player
            // ...

            // Delete the file
            if (file.delete()) {
                Log.d("StorageHelper", "File deleted: ${file.absolutePath}")
            } else {
                Log.e("StorageHelper", "Failed to delete file: ${file.absolutePath}")
            }
        } else {
            Log.w("StorageHelper", "File does not exist: ${file.absolutePath}")
        }
    }

    fun deleteCourseContent(courseId: Int) {
        val courseContentDir = File(context.filesDir, "course_content/$courseId")
        if (courseContentDir.exists()) {
            courseContentDir.deleteRecursively()
        }
    }
}