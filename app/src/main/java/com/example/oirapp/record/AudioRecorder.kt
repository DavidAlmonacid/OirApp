package com.example.oirapp.record

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

interface AudioRecorder {
    fun startRecording(outputFile: File)
    fun stopRecording()
}

class AudioRecorderImpl(private val context: Context) : AudioRecorder {
    private var recorder: MediaRecorder? = null

    private fun createRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }
    }

    override fun startRecording(outputFile: File) {
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioChannels(1)
            setAudioSamplingRate(16_000)
            setOutputFile(FileOutputStream(outputFile).fd)

            try {
                prepare()
                start()
            } catch (e: IOException) {
                println("AudioRecorderImpl.start: Error: ${e.message}")
            }

            recorder = this
        }
    }

    override fun stopRecording() {
        recorder?.apply {
            try {
                stop()
            } catch (e: Exception) {
                println("AudioRecorderImpl.stopRecording: Error: ${e.message}")
            } finally {
                release()
                recorder = null
            }
        }
    }
}
