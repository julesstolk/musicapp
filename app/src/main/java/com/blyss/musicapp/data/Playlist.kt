package com.blyss.musicapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Playlist(
    val title: String,
    val cover: Int? = null,
    val tracks: MutableList<Track> = mutableListOf()
) {
}