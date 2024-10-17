package com.charlesmuchogo.research.presentation.instructions.pdfViewer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.io.File

class PdfBitmapConverter(
    private val context: Context
) {
    var renderer: PdfRenderer? = null



    suspend fun pdfToBitmapsFromRaw(resourceId: Int): List<Bitmap> {
        return withContext(Dispatchers.IO) {
            renderer?.close()

            // Open the raw resource
            val inputStream = context.resources.openRawResource(resourceId)

            // Create a temporary file
            val tempFile = File(context.cacheDir, "temp_pdf.pdf")
            inputStream.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            // Open the temporary file as a ParcelFileDescriptor
            val descriptor = ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)

            with(PdfRenderer(descriptor)) {
                renderer = this

                return@withContext (0 until pageCount).map { index ->
                    async {
                        openPage(index).use { page ->
                            val bitmap = Bitmap.createBitmap(
                                page.width,
                                page.height,
                                Bitmap.Config.ARGB_8888
                            )

                            val canvas = Canvas(bitmap).apply {
                                drawColor(Color.WHITE)
                                drawBitmap(bitmap, 0f, 0f, null)
                            }

                            page.render(
                                bitmap,
                                null,
                                null,
                                PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                            )

                            bitmap
                        }
                    }
                }.awaitAll()
            }
        }
    }



    suspend fun pdfToBitmaps(contentUri: Uri): List<Bitmap> {
        return withContext(Dispatchers.IO) {
            renderer?.close()

            context
                .contentResolver
                .openFileDescriptor(contentUri, "r")
                ?.use { descriptor ->
                    with(PdfRenderer(descriptor)) {
                        renderer = this

                        return@withContext (0 until pageCount).map { index ->
                            async {
                                openPage(index).use { page ->
                                    val bitmap = Bitmap.createBitmap(
                                        page.width,
                                        page.height,
                                        Bitmap.Config.ARGB_8888
                                    )

                                    val canvas = Canvas(bitmap).apply {
                                        drawColor(Color.WHITE)
                                        drawBitmap(bitmap, 0f, 0f, null)
                                    }

                                    page.render(
                                        bitmap,
                                        null,
                                        null,
                                        PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                                    )

                                    bitmap
                                }
                            }
                        }.awaitAll()
                    }
                }
            return@withContext emptyList()
        }
    }
}