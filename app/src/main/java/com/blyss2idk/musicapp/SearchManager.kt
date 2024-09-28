package com.blyss2idk.musicapp

import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import android.util.Log

class SearchManager(private var context: Context) {

    fun cursorToTrackClass(cursor: Cursor): Track {
        val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
        val displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
        val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
        val duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
        val filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))

        return Track(id, displayName, title, artist, duration, filePath)
    }

    fun search(query: String): MutableList<Track> {

        val outputTracks = mutableListOf<Track>()

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,            // ID of the audio file
            MediaStore.Audio.Media.DISPLAY_NAME,   // Name of the audio file
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Audio.Media.RELATIVE_PATH
            } else {
                MediaStore.Audio.Media.DATA
            }
        )

        try {
            context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null
            )?.use { cursor ->
                Log.i("FILETEST", "yaya2")
                if (cursor.count == 0) {
                    Log.i("FILETEST", "empty")
                } else {
                    Log.i("FILETEST", "not empty")
                }
                while (cursor.moveToNext()) {
                    Log.i(
                        "FILETEST",
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                    )

                    outputTracks.add(cursorToTrackClass(cursor))

                }
            }
        } catch (e: IllegalArgumentException) {
            Log.e("FILETEST", "Query failed: ", e)
        }

        return outputTracks
    }
}