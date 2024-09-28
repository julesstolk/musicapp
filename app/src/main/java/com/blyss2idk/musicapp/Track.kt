package com.blyss2idk.musicapp

import android.database.Cursor
import android.graphics.Bitmap
import android.provider.MediaStore

data class Track(val fileId: Long,
                 val fileName: String,
                 val title: String?,
                 val artist: String?,
                 val duration: Long,
                 val filePath: String) {

    var hasMetadata = false
    var useMetadata = false
    lateinit var cover: Bitmap

    fun deserializeCursor(cursor: Cursor): Track {
        val fileId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
        val fileName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
        val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
        val duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
        val filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))

        if (title != null && artist != null) {
            useMetadata = true
        }

        return Track(fileId, fileName, title, artist, duration, filePath)
    }
}