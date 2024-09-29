package com.blyss2idk.musicapp.data

import androidx.compose.ui.graphics.Color

data class Theme(
    val title: String,
    val backgroundColor: Color,
    val textColor: Color,
    val tabsColor: Color,
    val tabsHorizontal: Int = 1,
    val tabsVertical: Int = 10,
    val tabsSize: Int = 70,
    val tabsSpacing: Int = 7,
    val searchBarColor: Color = tabsColor,
    val tabRoundedCornerShape: Int = 7,
    val searchButtonTextSpacing: Int = 10,
    val backGroundImage: Int? = null,
)