package com.blyss.musicapp.constant

import com.blyss.musicapp.data.Option
import com.blyss.musicapp.data.OptionType
import com.blyss.musicapp.R
import com.blyss.musicapp.data.Track
import com.blyss.musicapp.tools.PlayManager

class DefaultOptions() {

    val clearQueue = Option("clear queue", OptionType.BUTTON, icon=R.drawable.baseline_playlist_remove_24, onChange = { PlayManager.clearQueue() })

    fun createPlaylist(showPlaylistDialog: () -> Unit): Option {
        return Option(
            "create playlist",
            OptionType.BUTTON,
            icon = R.drawable.baseline_playlist_add_circle_24,
            onChange = { showPlaylistDialog() })
    }

    fun addTrackToQueue(track: Track): Option {
        return Option("add track to queue", OptionType.BUTTON, icon=R.drawable.baseline_playlist_add_24, onChange = { PlayManager.addQueue(track) }, )
    }

    fun addTrackToPlaylist(expandPlaylistDropdown: () -> Unit): Option {
        return Option("add track to playlist", OptionType.BUTTON, icon=R.drawable.baseline_playlist_add_24, onChange = { expandPlaylistDropdown() })
    }

    fun getAllSongOptions(track: Track, expandPlaylistDropdown: () -> Unit): List<Option> {
        val output = mutableListOf<Option>(
            addTrackToQueue(track),
            addTrackToPlaylist(expandPlaylistDropdown)
        )

        return output
    }

    fun getAllMoreOptions(showPlaylistDialog: () -> Unit): List<Option> {
        val output = mutableListOf<Option>(
            createPlaylist(showPlaylistDialog),
            clearQueue
        )

        return output
    }
}