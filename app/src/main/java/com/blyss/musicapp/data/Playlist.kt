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
    override val length: String = tracks.size.toString(),
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

    fun addTrack(track: Track) {
        tracks.add(track)
    }

    fun copyPlaylist(title: String): Playlist {
        return Playlist(title, cover, tracks = tracks.toMutableList(), ordered)
    }

    override fun play(context: Context) {
        PlayManager.startPlay(this, context)
    }
}