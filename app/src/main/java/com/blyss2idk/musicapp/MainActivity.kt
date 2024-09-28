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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.blyss2idk.musicapp.ui.theme.MusicappMain

class MainActivity : ComponentActivity() {

    private lateinit var sm: SearchManager

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
        // At the end, start up search manager
        sm = SearchManager(this)
    }

//    @Composable
//    fun NotifyNoPermissionSnackbar(permission: String) {
//        // to implement
//    }

    @Preview
    @Composable
    fun DefaultScreen() {
        val moreImg = R.drawable.baseline_more_vert_24
        val searchButtonGapSpacing = 10
        val amountTabs = 30
        val sizeButtonTabs = 70
        val buttonPadding = 7

        var query by remember {
            mutableStateOf("")
        }


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0))
        ) {

            // Top row with search bar and more options button
            item {
                Row (
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

                        }
                    ) {
                        Image(
                            painter = painterResource(id = moreImg),
                            contentDescription = "More"
                        )
                    }
                }
            }

            // Spacer for better design
            item {
                Spacer(modifier = Modifier.height(searchButtonGapSpacing.dp))
            }

            // Tabs for custom operations
            items (amountTabs) { index ->
                if (query == "") {
                    TabButton(index, sizeButtonTabs, buttonPadding)
                }
            }
        }
    }

    @Composable
    fun TabButton(id: Int, sizeButtonTabs: Int, buttonPadding: Int) {
        Button(
            modifier = Modifier
                .height(sizeButtonTabs.dp)
                .fillMaxWidth()
                .padding(buttonPadding.dp),
            onClick = {

            },
            shape = RoundedCornerShape(7.dp)
        ) {
            Text(
                text = id.toString()
            )
        }
    }

    @Composable
    fun StandardTab(heightTabs: Int,
                    paddingTabs: Int,
                    maintext: String,
                    secondText: String,
                    icon: Int,
                    tertiaryText: String,
                    buttons: List<@Composable () -> Unit>) {
        Row (
            modifier = Modifier
                .height(heightTabs.dp)
                .fillMaxWidth()
                .padding(paddingTabs.dp)
        ) {
            Column (
                modifier = Modifier
                    .weight(5f)
            ) {

            }

            for (button in buttons) {
                Box(
                    modifier = Modifier
                        .height(heightTabs.dp)
                        .width(heightTabs.dp)
                ) {
                    button()
                }
            }

        }
    }
}
