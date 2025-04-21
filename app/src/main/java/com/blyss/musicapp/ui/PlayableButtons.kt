package com.blyss.musicapp.ui

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import com.blyss.musicapp.R
import com.blyss.musicapp.constant.DefaultOptions
import com.blyss.musicapp.data.Playable
import com.blyss.musicapp.data.Playlist
import com.blyss.musicapp.data.Theme
import com.blyss.musicapp.data.Track

object PlayableButtons {

    lateinit var theme: Theme

    @Composable
    fun playableButtons(track: Track, optionsWithIcon: Int = 0): List<@Composable () -> Unit> {
        var expanded by remember {mutableStateOf(false)}
        var expanded2 by remember {mutableStateOf(false)}

        val allOptions = DefaultOptions.getAllSongOptions(track) { expanded2 = true }

        val output = mutableListOf<@Composable () -> Unit>()

        for (i in 0 until optionsWithIcon) {
            val option = allOptions[i]
            output.add({
                IconButton(onClick = { option.onChange() }) {
                    Icon(
                        painter = painterResource(option.icon),
                        contentDescription = option.title,
                        tint = theme.textColor
                    )
                }
            }
            )
        }

        output.add({
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    painter = painterResource(R.drawable.baseline_more_vert_24),
                    contentDescription = "more options",
                    tint = theme.textColor
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                for (option in allOptions) {
                    DropdownMenuItem(
                        text = { Text(option.title) },
                        onClick = {
                            option.onChange()
                            expanded = false
                        }
                    )
                }

                // ts pmo </3
                // PlaylistDropdownFactory.PlaylistDropdown(track, expanded2) { expanded2 = false }
            }
        }
        )

        return output
    }

    @Composable
    fun playableButtons(playable: Playable, optionsWithIcon: Int = 0): List<@Composable () -> Unit> {
        when (playable) {
            is Track -> return playableButtons(playable, optionsWithIcon)
            is Playlist -> null
        }
        return mutableListOf()
    }
}