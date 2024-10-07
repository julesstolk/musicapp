package com.blyss2idk.musicapp.tools

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.blyss2idk.musicapp.data.Track

object SearchManager {

    private val projection = arrayOf(
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

    private fun cursorToTrackClass(cursor: Cursor): Track {
        val id = cursor.getLong(cursor.getColumnIndexOrThrow(projection[0]))
        val displayName = cursor.getString(cursor.getColumnIndexOrThrow(projection[1]))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(projection[2]))
        val artist = cursor.getString(cursor.getColumnIndexOrThrow(projection[3]))
        val duration = cursor.getLong(cursor.getColumnIndexOrThrow(projection[4]))
        val filePath = cursor.getString(cursor.getColumnIndexOrThrow(projection[5]))

        val uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)

        return Track(id, displayName, title, artist, duration, filePath, uri)
    }

    fun search(context: Context, query: String): MutableList<Track> {

        val outputTracks = mutableListOf<Track>()

        val selection = "(${MediaStore.Audio.Media.DISPLAY_NAME} LIKE ? OR ${MediaStore.Audio.Media.TITLE} LIKE ?)"
        val selectionArgs = arrayOf("%${query}%", "%${query}%")

        try {
            context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
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