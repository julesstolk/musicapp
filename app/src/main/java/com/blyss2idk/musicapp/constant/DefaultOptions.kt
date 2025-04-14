package com.blyss2idk.musicapp.constant

import com.blyss2idk.musicapp.data.Option
import com.blyss2idk.musicapp.data.OptionType
import com.blyss2idk.musicapp.R
import com.blyss2idk.musicapp.data.Track
import com.blyss2idk.musicapp.tools.PlayManager

object DefaultOptions {

    val clearQueue = Option("clear queue", OptionType.BUTTON, icon=R.drawable.baseline_playlist_remove_24, onChange = { PlayManager.clearQueue() })

    fun createAddQueueGenerator(track: Track): Option {
        return Option("add track to queue", OptionType.BUTTON, icon=R.drawable.baseline_playlist_add_24, onChange = { PlayManager.addQueue(track) }, )
    }

    fun getAllSongOptions(track: Track): List<Option> {
        val output = mutableListOf<Option>()

        output.add(createAddQueueGenerator(track))

        return output
    }
}