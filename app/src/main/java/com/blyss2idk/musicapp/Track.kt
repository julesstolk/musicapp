package com.blyss2idk.musicapp

import android.graphics.Bitmap

class Track(val fileName: String,
            val fileId: Int,
            val filePath: String,
            val duration: Int,
            val title: String?,
            val artist: String?) {

    var hasMetadata = false
    var useMetadata = false
    lateinit var cover: Bitmap
}