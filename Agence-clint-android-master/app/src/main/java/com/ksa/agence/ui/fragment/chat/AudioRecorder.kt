package com.ksa.agence.ui.fragment.chat

import android.content.Context
import android.media.MediaRecorder
import java.io.File
import java.io.IOException

class AudioRecorder {
    private var mediaRecorder: MediaRecorder? = null
    private var outputFile: String? = null

    @Throws(IOException::class)
    fun startRecording(filePath: String) {
        outputFile = filePath
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(outputFile)
            prepare()
            start()
        }
    }

    fun stop() {
        mediaRecorder?.apply {
            stop()
            release()
            mediaRecorder = null
        }
    }



}
