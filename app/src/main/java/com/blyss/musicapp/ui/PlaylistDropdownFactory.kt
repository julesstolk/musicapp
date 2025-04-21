package com.blyss.musicapp.ui

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.blyss.musicapp.data.Track
import com.blyss.musicapp.tools.PlaylistManager

object PlaylistDropdownFactory {

    @Composable
    fun PlaylistDropdown(track: Track, expanded: Boolean, onDismissRequest: () -> Unit) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ) {
            val playlists = PlaylistManager.getPlaylists()

            for (playlist in playlists) {
                DropdownMenuItem(
                    text = { Text(playlist.title) },
                    onClick = {
                        playlist.addTrack(track)
                        onDismissRequest()
                    }
                )
            }
        }
    }
}