package com.blyss.musicapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.blyss.musicapp.constant.DefaultOptions
import com.blyss.musicapp.constant.DefaultThemes
import com.blyss.musicapp.data.TabType
import com.blyss.musicapp.data.Theme
import com.blyss.musicapp.tools.PlayManager
import com.blyss.musicapp.tools.PlaybackService
import com.blyss.musicapp.tools.RecentManager
import com.blyss.musicapp.tools.SearchManager
import com.blyss.musicapp.tools.Utils
import com.blyss.musicapp.ui.PlayableButtons
import com.blyss.musicapp.ui.PlaylistDialog
import com.blyss.musicapp.ui.StandardTab
import com.blyss.musicapp.ui.theme.MusicappMain
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private val theme = DefaultThemes.darkTheme2
    val showIconButtons = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        PlayManager.initPlayer(applicationContext)

        val intent = Intent(applicationContext, PlaybackService::class.java)
        ContextCompat.startForegroundService(applicationContext, intent)

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

    private fun setup(theme: Theme) {
        StandardTab.theme = theme
        PlaylistDialog.theme = theme
        PlayableButtons.theme = theme
    }

    @Composable
    fun DefaultScreen(context: Context) {
        setup(theme)

        val moreIcon = R.drawable.baseline_more_vert_24
        var query by remember {
            mutableStateOf("")
        }
        var songPlaying by remember {
            mutableStateOf(false)
        }
        var secondsSong by remember {
            mutableStateOf(Utils.getStringFromTime(PlayManager.getCurrentTimeInSeconds()))
        }
        var startedPlaying by remember{
            mutableStateOf(false)
        }
        var moreExpanded by remember {
            mutableStateOf(false)
        }
        var showDialog by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(songPlaying) {
            while(songPlaying) {
                while (true) {
                    delay(1000L)
                    val d = PlayManager.getCurrentTimeInSeconds()
                    secondsSong = Utils.getStringFromTime(d)
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
                                .weight(1f),
                            value = query,
                            onValueChange = { text ->
                                query = text
                            }
                        )

                        // Spacer(modifier = Modifier.weight(1f))

                        Box(
                            modifier = Modifier
                                .height(StandardTab.buttonSize)
                                .aspectRatio(1f)
                        ) {
                            IconButton(
                                modifier = Modifier
                                    .fillMaxSize(),
                                onClick = { moreExpanded = true }
                            ) {
                                Image(
                                    painter = painterResource(id = moreIcon),
                                    contentDescription = "more"
                                )
                                MoreOptionsDropdown(moreExpanded, { moreExpanded = false }, showDialog, { showDialog = true }, {showDialog = false})
                            }
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
                    if (RecentManager.getRecents().isEmpty()) {
                        item {
                            StandardTab.ErrorTab("no recents found.")
                        }
                    }
                    items(RecentManager.getRecents().size) { index ->
                        var playable = RecentManager.getRecents()[index]
                        StandardTab.StandardTab(
                            playable = playable,
                            onClick = {
                                playable.play(applicationContext)
                                startedPlaying = true
                                songPlaying = true
                                RecentManager.addRecent(playable)
                            },
                            buttons = PlayableButtons.playableButtons(playable, showIconButtons)
                        )
                    }

//                    items(theme.tabsVertical) { index ->
//                        StandardTab.StandardTab(TabType.SONG,
//                            index.toString(),
//                            "second text",
//                            "3:01")
//                    }
                }

                // Search results
                // Show only if there is a query
                if (query != "") {
                    val searchResult = SearchManager.searchFilesAndPlaylists(applicationContext, query)
                    if (searchResult.isEmpty()) {
                        item {
                            StandardTab.StandardTab(TabType.EXCEPTION, "no results.")
                        }
                    }
                    items(searchResult.size) { index ->
                        val result = searchResult[index]

                        StandardTab.StandardTab(
                            playable = result,
                            onClick = {
                                result.play(applicationContext)
                                startedPlaying = true
                                songPlaying = true
                                RecentManager.addRecent(result)
                            },
                            buttons = PlayableButtons.playableButtons(result, showIconButtons))
                    }
                }
            }


            // Current song playing
            // With buttons, on bottom of screen
            if (startedPlaying) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd),
                ) {
                    StandardTab.StandardTab(
                        TabType.CURRENT_SONG,
                        mainText = PlayManager.currentTrackPlaying()!!.fileName,
                        secondText = secondsSong,
                        thirdText = "",
                        onClick = {null},
                        buttons = listOf({
                            IconButton(onClick = {PlayManager.previousQueue(context)}) {
                                Icon(painter = painterResource(R.drawable.baseline_skip_previous_24),
                                    contentDescription = "previous queue",
                                    tint = theme.textColor)
                            }
                        }, {
                            IconButton(onClick = {
                                PlayManager.togglePlay()
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

    @Composable
    fun MoreOptionsDropdown(expanded: Boolean, onDismiss: () -> Unit, showDialog: Boolean, onShow: () -> Unit, onDismiss2: () -> Unit) {
        val allMoreOptions = DefaultOptions.getAllMoreOptions(onShow)

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss
        ) {
            for (option in allMoreOptions) {
                DropdownMenuItem(
                    text = { Text(option.title) },
                    onClick = {
                        option.onChange()
                        onDismiss()
                    }
                )
            }
        }

        PlaylistDialog.PlaylistDialog(showDialog) { onDismiss2() }
    }
}
