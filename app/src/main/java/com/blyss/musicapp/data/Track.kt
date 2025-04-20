package com.blyss.musicapp.data

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.blyss.musicapp.tools.PlayManager
import com.blyss.musicapp.tools.Utils
import kotlinx.serialization.Serializable
import java.io.FileNotFoundException

@Serializable
data class Track(val fileId: Long,
                 val fileName: String,
                 override val title: String,
                 val artist: String?,
                 val duration: Long,
                 val filePath: String?,
                 val id: Long,
                 override val length: String = Utils.getStringFromTime(duration),
                 override val thirdText: String = "",
                 override val type: TabType = TabType.SONG
) : Playable {

    var durationString: String
    var useMetadata = false


    // change this later to false and check uri's of all known tracks on launch or something
    var validUri = true
    val uri: Uri
        get() = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)


    fun verifyUri(context: Context) {
        val contentResolver = context.contentResolver

        validUri = try {
            contentResolver.openInputStream(uri)?.use {
                // Using 'use' to automatically close the InputStream after usage
                true
            } == true
        } catch (_: FileNotFoundException) {
            false
        }
    }

    val hasMetadata: Boolean
        get() = title.isNotEmpty() && !artist.isNullOrEmpty()

    override fun play(context: Context) {
        PlayManager.startPlay(this, context)
    }

    init {
        val d = duration / 1000
        val durationMinutes = d / 60
        var durationSeconds: String = (d % 60).toString()
        if (durationSeconds.length < 2) {
            durationSeconds = "0$durationSeconds"
        }
        durationString = "$durationMinutes:$durationSeconds"
    }
}