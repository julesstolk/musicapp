package com.blyss2idk.musicapp.data

import android.graphics.Bitmap
import android.net.Uri

data class Track(val fileId: Long,
                 val fileName: String,
                 val title: String?,
                 val artist: String?,
                 val duration: Long,
                 val filePath: String?,
                 val uri: Uri?) {

    var durationString: String
    var hasMetadata = false
    var useMetadata = false
    lateinit var cover: Bitmap

    init {
        if (title != null && artist != null) {
            hasMetadata = true
        }

        val d = duration / 1000
        val durationMinutes = d / 60
        val durationSeconds = d % 60
        durationString = "$durationMinutes:$durationSeconds"
    }
}