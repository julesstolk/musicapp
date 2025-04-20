package com.blyss.musicapp.tools

object Utils {
    fun getStringFromTime(d: Int): String {
        val durationMinutes = d / 60
        var durationSeconds: String = (d % 60).toString()
        if (durationSeconds.length < 2) {
            durationSeconds = "0$durationSeconds"
        }
        return "$durationMinutes:$durationSeconds"
    }

    fun getStringFromTime(d: Long): String {
        return getStringFromTime((d / 1000).toInt())
    }
}