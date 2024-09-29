package com.blyss2idk.musicapp.data

import android.graphics.Bitmap

data class Track(val fileId: Long,
                 val fileName: String,
                 val title: String?,
                 val artist: String?,
                 val duration: Long,
                 val filePath: String) {

    var hasMetadata = false
    var useMetadata = false
    lateinit var cover: Bitmap

    init {
        if (title != null && artist != null) {
            hasMetadata = true
        }
    }
}