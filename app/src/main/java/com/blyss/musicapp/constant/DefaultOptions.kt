package com.blyss.musicapp.constant

import com.blyss.musicapp.data.Option
import com.blyss.musicapp.data.OptionType
import com.blyss.musicapp.R
import com.blyss.musicapp.data.Playlist
import com.blyss.musicapp.data.Track
import com.blyss.musicapp.tools.PlayManager
import com.blyss.musicapp.tools.PlaylistManager

object DefaultOptions {

    private val clearQueue = Option("clear queue", OptionType.BUTTON, icon=R.drawable.baseline_playlist_remove_24, onChange = { PlayManager.clearQueue() })

    private fun createPlaylist(showPlaylistDialog: () -> Unit): Option {
        return Option(
            "create playlist",
            OptionType.BUTTON,
            icon = R.drawable.baseline_playlist_add_circle_24,
            onChange = { showPlaylistDialog() })
    }

    private fun addTrackToQueue(track: Track): Option {
        return Option("add track to queue", OptionType.BUTTON, icon=R.drawable.baseline_playlist_add_24, onChange = { PlayManager.addQueue(track) } )
    }

    private fun addTrackToPlaylist(expandPlaylistDropdown: () -> Unit): Option {
        return Option("add track to playlist", OptionType.BUTTON, icon=R.drawable.baseline_playlist_add_24, onChange = { expandPlaylistDropdown() })
    }

    private fun addTrackToPlaylistChoice(track: Track, playlist: Playlist): Option {
        return Option("+ " + playlist.title, OptionType.BUTTON, onChange = {
            PlayManager.addQueue(track)
            playlist.addTrack(track)})
    }

    fun getAllSongOptions(track: Track, mergePlaylistOptions: Boolean = true, expandPlaylistDropdown: () -> Unit): List<Option> {
        val output = mutableListOf<Option>(
            addTrackToQueue(track)
        )

        if (mergePlaylistOptions) {
            for (playlist in PlaylistManager.getPlaylists()) {
                output.add(addTrackToPlaylistChoice(track, playlist))
            }


        } else {
            output.add(addTrackToPlaylist(expandPlaylistDropdown))
        }

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