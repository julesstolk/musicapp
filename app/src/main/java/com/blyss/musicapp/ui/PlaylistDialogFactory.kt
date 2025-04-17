package com.blyss.musicapp.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import com.blyss.musicapp.R
import com.blyss.musicapp.constant.DefaultOptions
import com.blyss.musicapp.data.TabType
import com.blyss.musicapp.data.Theme
import com.blyss.musicapp.tools.PlaylistManager

class PlaylistDialogFactory(val standardTabFactory: StandardTabFactory,
                            val defaultOptions: DefaultOptions,
                            val theme: Theme) {
    val option = defaultOptions.createPlaylist


    @Composable
    fun PlaylistDialog(showDialog: Boolean = true) {
        var text by remember {
            mutableStateOf("")
        }
        var showDialog by remember {
            mutableStateOf(showDialog)
        }

        if (showDialog) {
            Dialog(
                onDismissRequest = { text = "" }
            ) {
                TextField(
                    value = text,
                    onValueChange = { newText ->
                        text = newText
                    }
                )
                standardTabFactory.StandardTab(
                    tabType = TabType.PLAYLIST,
                    mainText = "enter new playlist name",
                    secondaryText = "or press cancel idk 🤣🤣🤣🤣",
                    tertiaryText = "",
                    onClick = { null },
                    icon = option.icon,
                    buttons = listOf(
                        {
                            IconButton(onClick = {
                                showDialog = false
                            }) {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_cancel_24),
                                    contentDescription = "cancel create playlist",
                                    tint = theme.textColor
                                )
                            }
                        }, {
                            IconButton(onClick = {
                                PlaylistManager.createPlaylist(text, ordered = false)
                                showDialog = false
                            }) {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_playlist_add_circle_24),
                                    contentDescription = "confirm create playlist",
                                    tint = theme.textColor
                                )
                            }
                        }
                    )
                )
            }
        }
    }
}