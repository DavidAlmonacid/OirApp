package com.example.oirapp.record

interface AudioRecorder {
    fun start()
    fun stop()
}

class AudioRecorderImpl : AudioRecorder {
    override fun start() {
        println("Recording started")
    }

    override fun stop() {
        println("Recording stopped")
    }
}
