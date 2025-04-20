package com.blyss.musicapp.tools

import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.blyss.musicapp.data.Playable
import com.blyss.musicapp.data.Playlist
import com.blyss.musicapp.data.Track

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

        val track = Track(fileId = id, fileName = displayName, title = title, artist = artist, duration = duration, filePath = filePath, id = id)

        return track
    }

    fun searchFiles(context: Context, query: String): MutableList<Track> {

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
                while (cursor.moveToNext()) {

                    outputTracks.add(cursorToTrackClass(cursor))

                }
            }
        } catch (e: IllegalArgumentException) {
            Log.e("FILETEST", "Query failed: ", e)
        }

        return outputTracks
    }

    fun searchPlaylist(query: String): List<Playlist> {
        // todo implement search engine or something maybe idk
        val output = mutableListOf<Playlist>()
        val playlists = PlaylistManager.getPlaylists()

        for (playlist in playlists) {
            if (playlist.title.lowercase().contains(query)) {
                output.add(playlist)
            }
        }
        output.sortBy { it.title }

        return output
    }

    fun searchFilesAndPlaylists(context: Context, query: String): List<Playable> {
        val playlists = searchPlaylist(query)
        val files = searchFiles(context, query)

        val merged = mutableListOf<Playable>()
        merged.addAll(playlists)
        merged.addAll(files)

        return merged
    }
}