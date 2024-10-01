package com.blyss2idk.musicapp.data

import com.blyss2idk.musicapp.R

enum class TabType {
    SONG {
        fun icon() = R.drawable.baseline_music_note_24
    },
    PLAYLIST {
        fun icon() = R.drawable.baseline_library_music_24
    },
    PREFERENCE {
        fun icon() = R.drawable.baseline_settings_suggest_24
    },
    EXCEPTION {
        fun icon() = R.drawable.baseline_cancel_24
    }
}