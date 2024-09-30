package com.blyss2idk.musicapp.data

import androidx.compose.ui.graphics.Color

class DefaultThemes {
    companion object {
        val darkTheme = Theme(title = "dark minimal", backgroundColor = Color.Black, textColor = Color.White, tabColor = Color.Gray, tabAlpha = 1f, tabsHorizontal = 2, tabRoundedCornerShape = 2, tabPadding = 2)
        val darkTheme2 = Theme(title = "dark", backgroundColor = Color.Black, textColor = Color.White, tabColor = Color.Gray.copy(alpha=0.5f), tabAlpha = 0.9f, tabsHorizontal = 2, tabRoundedCornerShape = 2, tabPadding = 2, backgroundImage = "bg.png")
    }
}