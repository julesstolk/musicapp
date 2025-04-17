package com.blyss.musicapp.constant

import com.blyss.musicapp.data.Option
import com.blyss.musicapp.data.OptionType
import com.blyss.musicapp.R
import com.blyss.musicapp.data.Track
import com.blyss.musicapp.tools.PlayManager

class DefaultOptions(val showPlaylistDialog: () -> Unit) {

    val clearQueue = Option("clear queue", OptionType.BUTTON, icon=R.drawable.baseline_playlist_remove_24, onChange = { PlayManager.clearQueue() })
    val createPlaylist = Option("create playlist", OptionType.BUTTON, icon=R.drawable.baseline_playlist_add_circle_24, onChange = {showPlaylistDialog})

    fun createAddQueueGenerator(track: Track): Option {
        return Option("add track to queue", OptionType.BUTTON, icon=R.drawable.baseline_playlist_add_24, onChange = { PlayManager.addQueue(track) }, )
    }

    fun getAllSongOptions(track: Track): List<Option> {
        val output = mutableListOf<Option>(
            createAddQueueGenerator(track)
        )

        return output
    }

    fun getAllMainOptions(): List<Option> {
        val output = mutableListOf<Option>(
            createPlaylist,
            clearQueue
        )

        return output
    }
}