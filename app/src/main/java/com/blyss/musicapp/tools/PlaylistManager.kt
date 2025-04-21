package com.blyss.musicapp.tools

import com.blyss.musicapp.data.Playlist

object PlaylistManager {
    private val playlists = mutableListOf<Playlist>()

    fun getPlaylists(): List<Playlist> {
        return playlists
    }

    fun createPlaylist(title: String, cover: Int? = null, ordered: Boolean): Playlist {
        val playlist = Playlist(title, cover, ordered = ordered)
        playlists.add(playlist)
        return playlist
    }

    fun copyPlaylist(playlist: Playlist, title: String): Playlist {
        val newPlaylist = Playlist(title, playlist.cover, playlist.getTracks().toMutableList(), playlist.ordered)
        playlists.add(newPlaylist)
        return newPlaylist
    }

    fun savePlaylists(): Boolean {
        // save playlists to json
        // use dao
        // return successful t/f
        return false
    }

    init {
        // load playlists using dao
    }
}