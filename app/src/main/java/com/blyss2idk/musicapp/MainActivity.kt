package com.blyss2idk.musicapp

import android.Manifest
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.blyss2idk.musicapp.data.DefaultThemes
import com.blyss2idk.musicapp.data.TabType
import com.blyss2idk.musicapp.tools.SearchManager
import com.blyss2idk.musicapp.ui.theme.MusicappMain

class MainActivity : ComponentActivity() {

    private val theme = DefaultThemes.darkTheme2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MusicappMain {
                DefaultScreen()
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

    @Preview
    @Composable
    fun DefaultScreen() {
        val moreIcon = R.drawable.baseline_more_vert_24
        var query by remember {
            mutableStateOf("")
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
                            .padding(vertical = 10.dp),
                        text = if (query == "") "main" else "searching...",
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
                                .weight(5f),
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
                                contentDescription = "More"
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
                    if (searchResult.size == 0) {
                        item {
                            StandardTab(TabType.EXCEPTION, "no results.")
                        }
                    }
                    items(searchResult.size) { index ->
                        val result = searchResult[index]
                        if (result.useMetadata) {

                        } else {
                            StandardTab(
                                TabType.SONG,
                                result.fileName,
                                "",
                                result.durationString
                            )
                        }
                    }
                }
            }
        }
    }

    fun todo() {}

    @Composable
    fun StandardTab(tabType: TabType,
                    mainText: String,
                    secondaryText: String = "",
                    tertiaryText: String = "",
                    // REMOVE AUTOMATIC ARGUMENT LATER
                    onClick: () -> Unit = { todo() },
                    icon: Int? = null,
                    buttons: List<@Composable () -> Unit> = listOf()) {

        val songIcon = R.drawable.baseline_music_note_24
        val playlistIcon = R.drawable.baseline_library_music_24

        // Row containing texts and buttons

        Row(
            modifier = Modifier
                .height(theme.tabSizeVertical.dp)
                .fillMaxWidth()
                .padding(end = theme.tabPadding.dp)
                .padding(vertical = theme.tabPadding.dp)
                .clip(RoundedCornerShape(theme.tabRoundedCornerShape.dp))
                .background(theme.tabColor)
                .alpha(theme.tabAlpha)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onClick()
                    }
            ) {
                Row {

                    if (icon == null) {
                        if (tabType == TabType.SONG) {
                            Image(
                                painter = painterResource(songIcon),
                                contentDescription = "song icon",
                                modifier = Modifier
                                    .height(theme.tabSizeVertical.dp)
                            )
                        } else if (tabType == TabType.PLAYLIST) {
                            Image(
                                painter = painterResource(playlistIcon),
                                contentDescription = "playlist icon"
                            )
                        }
                    } else {
                        Image(
                            painter = painterResource(icon),
                            contentDescription = "customIcon"
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        // First text
                        Text(
                            text = mainText,
                            fontSize = theme.textSize.sp,
                            color = theme.textColor
                        )

                        // Second text
                        Text(
                            text = secondaryText,
                            color = theme.textColor
                        )
                    }

                    // Third text
                    Text(
                        text = tertiaryText,
                        modifier = Modifier
                            .align(Alignment.CenterVertically),
                        color = theme.textColor
                    )
                }
            }

            for (button in buttons) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                ) {
                    button()
                }
            }
        }
    }
}
