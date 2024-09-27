package com.blyss2idk.musicapp.ui.theme

import java.io.File
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity

class SearchManager(private var context: ComponentActivity) {

    // Change type
    var knownFiles = mutableListOf<File>()

    fun refresh() {

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,            // ID of the audio file
            MediaStore.Audio.Media.DISPLAY_NAME,   // Name of the audio file
            MediaStore.Audio.Media.DATA            // Full file path to audio file
        )

        context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            Log.i("FILETEST", "yaya2")
            while (cursor.moveToNext()) {
                Log.i("FILETEST", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)))
            }
        }
    }
}