package com.blyss2idk.musicapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.blyss2idk.musicapp.constant.DefaultOptions
import com.blyss2idk.musicapp.constant.DefaultThemes
import com.blyss2idk.musicapp.data.TabType
import com.blyss2idk.musicapp.data.Track
import com.blyss2idk.musicapp.tools.PlayManager
import com.blyss2idk.musicapp.tools.SearchManager
import com.blyss2idk.musicapp.ui.theme.MusicappMain
import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.math.min

class MainActivity : ComponentActivity() {

    private val theme = DefaultThemes.darkTheme2
    val showIconButtons = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MusicappMain {
                DefaultScreen(this)
            }
        }

        checkPermissions()
    }

    // Ask for permissions for reading audio media
    private fun checkPermissions() {
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission())
            { isGranted: Boolean ->
                if (!isGranted) {
                    // Permission not granted, show snackbar that says that app is unusable
                    // and/or close app
                    // NotifyNoPermissionSnackbar()
                }
            }
        // Check android version to support both older as newer android versions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Ask permission if not granted, else do nothing
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    // If permission not already granted, ask for permission
                    requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
            }
        }
        else {
            // Ask permission READ_EXTERNAL_STORAGE instead, for older android versions
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // Ask permission if not granted, else do nothing
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // If permission not already granted, ask for permission
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }
    }

    @Composable
    fun DefaultScreen(context: Context) {
        val moreIcon = R.drawable.baseline_more_vert_24
        var query by remember {
            mutableStateOf("")
        }
        var songPlaying by remember {
            mutableStateOf(false)
        }
        var secondsSong by remember {
            mutableStateOf(getStringFromTime(PlayManager.getCurrentTimeInSeconds()))
        }
        var startedPlaying by remember{
            mutableStateOf(false)
        }

        LaunchedEffect(songPlaying) {
            while(songPlaying) {
                while (true) {
                    delay(1000L)
                    val d = PlayManager.getCurrentTimeInSeconds()
                    secondsSong = getStringFromTime(d)
                }
            }
        }

        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(theme.backgroundColor)
        ) {
            if (theme.backgroundImage != null) {
                Image(
                    painter = painterResource(id = theme.backgroundImage),
                    contentDescription = "background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Title bar for phones with untouchable screen on top

                item {
                    Text(
                        modifier = Modifier
                            .padding(vertical = theme.topBarSpacing.dp),
                        text = if (query == "") "main" else "searching",
                        color = theme.textColor
                    )
                }


                // Top row with search bar and more options button
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            modifier = Modifier
                                .weight(6f),
                            value = query,
                            onValueChange = { text ->
                                query = text
                            }
                        )
                        IconButton(
                            modifier = Modifier
                                .weight(1f),
                            onClick = {
                                todo()
                            }
                        ) {
                            Image(
                                painter = painterResource(id = moreIcon),
                                contentDescription = "more"
                            )
                        }
                    }
                }

                // Spacer for better design
                item {
                    Spacer(modifier = Modifier.height(theme.searchButtonTextSpacing.dp))
                }

                // Tabs for custom functions
                // Show only if there is no query
                // Implement functions !!!
                // Hardcoded on TabType.SONG CHANGE LATER
                if (query == "") {
                    items(theme.tabsVertical) { index ->
                        StandardTab(TabType.SONG,
                            index.toString(),
                            "second text",
                            "3:01")
                    }
                }

                // Search results
                // Show only if there is a query
                if (query != "") {
                    val searchResult = SearchManager.search(applicationContext, query)
                    if (searchResult.isEmpty()) {
                        item {
                            StandardTab(TabType.EXCEPTION, "no results.")
                        }
                    }
                    items(searchResult.size) { index ->
                        val result = searchResult[index]
                        if (result.useMetadata) {
                            // will probably stay irrelevant idk
                        } else {
                            StandardTab(
                                TabType.SONG,
                                result.fileName,
                                //result.fileName.slice(0..(max(5, min(result.fileName.length - 1, 34 - 5*showIconButtons)))),
                                result.durationString,
                                "",
                                onClick = {
                                    PlayManager.startPlay(result, applicationContext)
                                    startedPlaying = true
                                    songPlaying = true
                                },
                                buttons = listOf { SongButtonsGenerator(result, showIconButtons) }
                            )
                        }
                    }
                }
            }

            if (startedPlaying) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd),
                ) {
                    StandardTab(
                        TabType.CURRENT_SONG,
                        mainText = PlayManager.currentTrackPlaying()!!.fileName,
                        secondaryText = secondsSong,
                        tertiaryText = "",
                        onClick = {null},
                        buttons = listOf({
                            IconButton(onClick = {PlayManager.previousQueue(context)}) {
                                Icon(painter = painterResource(R.drawable.baseline_skip_previous_24),
                                    contentDescription = "previous queue",
                                    tint = theme.textColor)
                            }
                        }, {
                            IconButton(onClick = {
                                PlayManager.togglePlay(context)
                                songPlaying = !songPlaying
                            }) {
                                if (songPlaying) {
                                    Icon(
                                        painter = painterResource(R.drawable.baseline_pause_24),
                                        contentDescription = "pause track",
                                        tint = theme.textColor
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(R.drawable.baseline_play_arrow_24),
                                        contentDescription = "play track",
                                        tint = theme.textColor
                                    )
                                }
                            }
                        }, {
                            IconButton(onClick = {PlayManager.nextQueue(context)}) {
                                Icon(painter = painterResource(R.drawable.baseline_skip_next_24),
                                    contentDescription = "next queue",
                                    tint = theme.textColor)
                            }
                        })
                    )
                }
            }
        }
    }

    fun todo() {}

    @Composable
    fun StandardTab(
        tabType: TabType,
        mainText: String,
        secondaryText: String = "",
        tertiaryText: String = "",
        onClick: () -> Unit = {},
        icon: Int? = null,
        background: Int? = null,
        buttons: List<@Composable (() -> Unit)>? = null,
    ) {
        var useIcon = tabType.icon()
        if (icon != null) {
            useIcon = icon
        }

        Row(
            modifier = Modifier
                .height(theme.tabSizeVertical.dp)
                .fillMaxWidth()
                .padding(horizontal = theme.tabPadding.dp, vertical = theme.tabPadding.dp)
                .clip(RoundedCornerShape(theme.tabRoundedCornerShape.dp))
                .background(theme.tabColor)
                .alpha(theme.tabAlpha),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(useIcon),
                contentDescription = tabType.toString(),
                modifier = Modifier
                    .height((theme.tabSizeVertical * 0.6).dp)
                    .aspectRatio(1F)
                    .padding(end = 8.dp),
                tint = theme.textColor
            )

            // Middle: Texts (clickable area)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onClick() }
            ) {
                Text(
                    text = mainText,
                    fontSize = theme.textSize.sp,
                    color = theme.textColor,
                    maxLines = 1
                )
                Text(
                    text = secondaryText,
                    color = theme.textColor,
                    maxLines = 1
                )
            }

            // Right: Tertiary text
            if (tertiaryText.isNotBlank()) {
                Text(
                    text = tertiaryText,
                    color = theme.textColor,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    maxLines = 1
                )
            }

            // Buttons
            if (buttons != null) {
                for (button in buttons) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            //.weight(1f)
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        button()
                    }
                }
            }
        }
    }


    fun getStringFromTime(d: Int): String {
        val durationMinutes = d / 60
        var durationSeconds: String = (d % 60).toString()
        if (durationSeconds.length < 2) {
            durationSeconds = "0$durationSeconds"
        }
        return "$durationMinutes:$durationSeconds"
    }

    @Composable
    fun SongButtonsGenerator(track: Track, optionsWithIcon: Int = 0) {
        val allOptions = DefaultOptions.getAllSongOptions(track)
        var expanded by remember { mutableStateOf(false) }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0 until optionsWithIcon) {
                val option = allOptions[i]
                Box (
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    IconButton(onClick = { option.onChange() }) {
                        Icon(
                            painter = painterResource(option.icon),
                            contentDescription = option.title,
                            tint = theme.textColor
                        )
                    }
                }
            }
        }

        Box (
            modifier = Modifier
                .fillMaxSize()
        ) {
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
            }
        }
    }

}
