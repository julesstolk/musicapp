package com.blyss.musicapp.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.blyss.musicapp.R
import com.blyss.musicapp.data.Theme
import com.blyss.musicapp.tools.PlaylistManager

object PlaylistDialogFactory {

    lateinit var theme: Theme

    val buttonSize = 36.dp

    @Composable
    fun PlaylistDialog(showDialog: Boolean, onDismiss: () -> Unit) {
        var text by remember {
            mutableStateOf("")
        }

        if (showDialog) {
            Dialog(
                onDismissRequest = {
                    text = ""
                    onDismiss()
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TextField(
                        value = text,
                        onValueChange = { newText ->
                            text = newText
                        },
                        placeholder = { Text(text = "enter playlist name...") },
                        modifier = Modifier.weight(1f)
                    )

                    // Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        modifier = Modifier
                            .height(buttonSize)
                            .aspectRatio(1f)
                            .align(Alignment.CenterVertically),
                        onClick = {
                            onDismiss()
                        }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_cancel_24),
                            contentDescription = "cancel create playlist",
                            tint = theme.textColor
                        )
                    }
                    IconButton(
                        modifier = Modifier
                            .height(buttonSize)
                            .aspectRatio(1f)
                            .align(Alignment.CenterVertically),
                        onClick = {
                        PlaylistManager.createPlaylist(text, ordered = false)
                        onDismiss()
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_playlist_add_circle_24),
                            contentDescription = "confirm create playlist",
                            tint = theme.textColor
                        )
                    }
                }
            }
        }
    }
}