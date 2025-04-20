package com.blyss.musicapp.data

import android.content.Context

interface Playable {
    val title: String
    val length: String
    val thirdText: String

    val type: TabType

    fun play(context: Context)
}