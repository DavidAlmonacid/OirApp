package com.example.oirapp.report

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.example.oirapp.data.network.Message
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun generatePdf(context: Context, messages: List<Message>, groupName: String) {
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

    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val file = File(downloadsDir, "Reporte Grupo_$groupName.pdf")

    try {
        pdfDocument.writeTo(FileOutputStream(file))
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        pdfDocument.close()
    }
}
