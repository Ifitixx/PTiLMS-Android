package com.pizzy.ptilms

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.*

class PdfBitmapConverter(private val context: Context) {

    suspend fun pdfToBitmaps(pdfUri: Uri): List<Bitmap> = withContext(Dispatchers.IO) {
        val bitmaps = mutableListOf<Bitmap>()
        try {
            context.contentResolver.openFileDescriptor(pdfUri, "r")?.use { parcelFileDescriptor ->
                val pdfRenderer = PdfRenderer(parcelFileDescriptor)
                for (pageIndex in 0 until pdfRenderer.pageCount) {
                    val page = pdfRenderer.openPage(pageIndex)
                    val bitmap = renderPage(page)
                    bitmaps.add(bitmap)
                    page.close()
                }
                pdfRenderer.close()
            }
        } catch (e: Exception) {
            Log.e("PdfUtils", "Cannot read from file descriptor. Error: ${e.message}", e)
        }
        bitmaps
    }

    private fun renderPage(page: PdfRenderer.Page): Bitmap {
        val width = page.width
        val height = page.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        return bitmap
    }
}