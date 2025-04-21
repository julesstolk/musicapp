package com.blyss.musicapp.data

import android.content.Context
import com.blyss.musicapp.tools.PlayManager
import kotlinx.serialization.Serializable

@Serializable
data class Playlist(
    override val title: String,
    val cover: Int? = null,
    private val tracks: MutableList<Track> = mutableListOf(),
    val ordered: Boolean = false,
    override var length: String = tracks.size.toString() + " tracks",
    override val thirdText: String = "",
    override val type: TabType = TabType.PLAYLIST
) : Playable {

    fun getTracks(): List<Track> {
        if (!ordered) {
            val shuffled = tracks
            shuffled.shuffle()
            return shuffled
        }
        return tracks
    }

    fun size(): Int {
        return tracks.size
    }

    fun addTrack(track: Track) {
        tracks.add(track)
        length = tracks.size.toString() + " tracks"
    }

    fun removeTrack(track: Track) {
        tracks.remove(track)
        length = tracks.size.toString() + " tracks"
    }

    override fun play(context: Context) {
        PlayManager.startPlay(this, context)
    }
}