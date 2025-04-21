package com.blyss.musicapp.constant

import androidx.compose.ui.graphics.Color
import com.blyss.musicapp.R
import com.blyss.musicapp.data.Theme

object DefaultThemes {

    val darkTheme = Theme(
        title = "dark minimal",
        backgroundColor = Color.Companion.Black,
        textColor = Color.Companion.White,
        tabColor = Color.Companion.Gray,
        tabAlpha = 1f,
        tabsHorizontal = 2,
        tabRoundedCornerShape = 2,
        tabPadding = 2
    )
    val darkTheme2 = Theme(
        title = "dark",
        backgroundColor = Color.Companion.Black,
        textColor = Color.Companion.White,
        tabColor = Color.Companion.Gray.copy(alpha = 0.5f),
        tabAlpha = 0.9f,
        tabsHorizontal = 2,
        tabRoundedCornerShape = 2,
        tabPadding = 2,
        backgroundImage = R.drawable.bg2,
        tabSizeVertical = 60,
        textSize = 20f,
        tabBackground = R.drawable.ornament
    )

}