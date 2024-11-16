package com.example.oirapp.report

import android.content.ContentValues
import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.example.oirapp.data.network.Message
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun generatePdf(
    messages: List<Message>,
    groupName: String,
    context: Context,
) {
    val pdfDocument = PdfDocument()

    try {
        val pageHeight = 792
        val pageWidth = 612
        val paint = Paint()
        val title = Paint()

        // Initialize first page
        var currentPage = pdfDocument.startPage(
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        )
        var canvas = currentPage.canvas
        val xPosition = 44f
        val topBottomMargin = 52f
        val maxWidth = pageWidth - (xPosition * 2)

        title.textSize = 16f
        title.isFakeBoldText = true
        canvas.drawText("Reporte del grupo: $groupName", xPosition, topBottomMargin, title)

        paint.textSize = 12f
        var yPosition = 96f

        for (message in messages) {
            val prefix = "${message.senderInfo.name} | ${message.senderInfo.role}: "
            val fullMessage = prefix + message.message
            val words = fullMessage.split(" ")
            var currentLine = StringBuilder()

            words.forEach { word ->
                val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
                val measureWidth = paint.measureText(testLine)

                if (measureWidth <= maxWidth) {
                    if (currentLine.isNotEmpty()) currentLine.append(" ")
                    currentLine.append(word)
                } else {
                    if (currentLine.isNotEmpty()) {
                        canvas.drawText(currentLine.toString(), xPosition, yPosition, paint)
                        yPosition += paint.descent() - paint.ascent() + 8
                    }
                    currentLine = StringBuilder(word)
                }

                // Check if we need a new page
                if (yPosition > pageHeight - topBottomMargin) {
                    try {
                        // Finish current page
                        pdfDocument.finishPage(currentPage)

                        // Create new page
                        val pageNumber = pdfDocument.pages.size + 1
                        val newPageInfo =
                            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
                        currentPage = pdfDocument.startPage(newPageInfo)
                        canvas = currentPage.canvas
                        yPosition = topBottomMargin // Reset Y position for new page
                    } catch (e: Exception) {
                        e.printStackTrace()
                        throw IOException("Error creating new page: ${e.message}")
                    }
                }
            }

            // Draw remaining text
            if (currentLine.isNotEmpty()) {
                canvas.drawText(currentLine.toString(), xPosition, yPosition, paint)
            }
            yPosition += paint.descent() - paint.ascent() + 16
        }

        // Finish last page
        pdfDocument.finishPage(currentPage)

        // Use content resolver for Android 10+ (API 29+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, "Reporte Grupo_$groupName.pdf")
                put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                put(MediaStore.Downloads.IS_PENDING, 1)
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            uri?.let {
                resolver.openOutputStream(it)?.use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }

                contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
                resolver.update(it, contentValues, null, null)
            }
        } else {
            // Legacy approach for Android 9 and below
            val downloadsDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, "Reporte Grupo_$groupName.pdf")
            FileOutputStream(file).use { outputStream ->
                pdfDocument.writeTo(outputStream)
            }
        }

        pdfDocument.close()
        Toast.makeText(context, "PDF generado exitosamente", Toast.LENGTH_SHORT).show()
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Error al generar el PDF: ${e.message}", Toast.LENGTH_SHORT).show()
    } finally {
        try {
            pdfDocument.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
