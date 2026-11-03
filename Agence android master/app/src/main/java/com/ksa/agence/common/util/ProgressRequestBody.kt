package com.ksa.nafhaseha.common.util

import android.os.Handler
import android.os.Looper
import android.util.Log

import java.io.File
import java.io.FileInputStream
import java.io.IOException

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink


class ProgressRequestBody(private val mFile: File, private val mListener: UploadCallbacks) : RequestBody() {
    private val mPath: String? = null

    interface UploadCallbacks {
        fun onProgressUpdate(percentage: Int)
        fun onError()
        fun onFinish()
    }

    override fun contentType(): MediaType? {
        // i want to upload only images
        return "image/*".toMediaTypeOrNull()
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return mFile.length()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val fileLength = mFile.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val `in` = FileInputStream(mFile)
        var uploaded: Long = 0

        try {
            var read: Int=0
            val handler = Handler(Looper.getMainLooper())
            while (run {
                    read = `in`.read(buffer)
                    read
                } != -1) {

                // update progress on UI thread
                handler.post(ProgressUpdater(uploaded, fileLength))

                uploaded += read.toLong()
                sink.write(buffer, 0, read)
            }
        } finally {
            `in`.close()
        }
    }

    private inner class ProgressUpdater(private val mUploaded: Long, private val mTotal: Long) : Runnable {

        override fun run() {
            mListener.onProgressUpdate((100 * mUploaded / mTotal).toInt())
            Log.e("progresss>>>>", "" + 100 * mUploaded / mTotal)
        }
    }

    companion object {

        private val DEFAULT_BUFFER_SIZE = 1024
    }
}
