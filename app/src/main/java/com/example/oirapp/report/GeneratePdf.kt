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
    try {
        val pageHeight = 792
        val pageWidth = 612
        val pdfDocument = PdfDocument()
        val paint = Paint()
        val title = Paint()
        val myPageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val myPage = pdfDocument.startPage(myPageInfo)
        val canvas = myPage.canvas

        title.textSize = 24f
        canvas.drawText("Reporte del grupo $groupName", 40f, 40f, title)

        paint.textSize = 12f
        var yPosition = 100f

        for (message in messages) {
            val formattedMessage = "${message.senderInfo.name}: ${message.message}"
            canvas.drawText(formattedMessage, 80f, yPosition, paint)
            yPosition += paint.descent() - paint.ascent()
        }

        pdfDocument.finishPage(myPage)

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
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, "Reporte Grupo_$groupName.pdf")
            FileOutputStream(file).use { outputStream ->
                pdfDocument.writeTo(outputStream)
            }
        }

        pdfDocument.close()
        // Show success message
        Toast.makeText(context, "PDF generado exitosamente", Toast.LENGTH_SHORT).show()

    } catch (e: IOException) {
        e.printStackTrace()
        // Show error message
        Toast.makeText(context, "Error al generar el PDF: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}
