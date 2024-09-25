package com.blyss2idk.musicapp

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.blyss2idk.musicapp.ui.theme.MusicappMain
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MusicappMain {
                DefaultScreen()
            }
        }

        if (ContextCompat.checkSelfPermission(this, "READ_EXTERNAL_AUDIO") != PackageManager.PERMISSION_GRANTED) {
            val activityresultcallback = ARC()
        }
    }

    class ARC : ActivityResultCallback<Boolean> {
        override fun onActivityResult(result: Boolean) {
            if (!result) {
                exitProcess(0)
            }
        }

    }

    @Preview
    @Composable
    fun DefaultScreen() {
        val moreImg = R.drawable.baseline_more_vert_24
        val searchButtonGapSpacing = 10
        val amountTabs = 10
        val sizeButtonTabs = 70
        val buttonPadding = 7

        var query by remember {
            mutableStateOf("")
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
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

            Spacer(modifier = Modifier.height(searchButtonGapSpacing.dp))

            for (i in 1..amountTabs) {
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
                        text = i.toString()
                    )
                }
            }
        }
    }
}
