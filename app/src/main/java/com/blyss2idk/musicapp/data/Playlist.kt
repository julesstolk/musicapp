package com.blyss2idk.musicapp.data

data class Playlist(
    val title: String,
    val cover: Int? = null,
    val tracks: MutableList<Track> = mutableListOf()
) {
}