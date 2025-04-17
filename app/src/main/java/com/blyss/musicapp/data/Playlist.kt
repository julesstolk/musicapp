package com.blyss.musicapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Playlist(
    val title: String,
    val cover: Int? = null,
    val tracks: MutableList<Track> = mutableListOf(),
    val ordered: Boolean = false
) {
    fun copyPlaylist(title: String): Playlist {
        return Playlist(title, cover, tracks = tracks.toMutableList(), ordered)
    }
}