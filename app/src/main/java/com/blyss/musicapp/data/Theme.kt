package com.blyss.musicapp.data

import androidx.compose.ui.graphics.Color

data class Theme(
    // no tabSizeHorizontal; fill max width
    val title: String,
    val backgroundColor: Color,
    val textColor: Color,
    val textSize: Float = 10f,
    val tabColor: Color = Color.Gray,
    val tabAlpha: Float = 1f,
    val tabsHorizontal: Int = 1,
    val tabsVertical: Int = 10,
    val tabSizeVertical: Int = 70,
    val tabPadding: Int = 7,
    val searchBarColor: Color = tabColor,
    val tabRoundedCornerShape: Int = 7,
    val searchButtonTextSpacing: Int = 10,
    val backgroundImage: Int? = null,
    val topBarSpacing: Int = 20,
    val tabBackground: Int? = null
)