package com.blyss.musicapp.data

import com.blyss.musicapp.R

enum class TabType {
    SONG {
        override fun icon() = R.drawable.baseline_music_note_24
    },
    PLAYLIST {
        override fun icon() = R.drawable.baseline_library_music_24
    },
    PREFERENCE {
        override fun icon() = R.drawable.baseline_settings_suggest_24
    },
    EXCEPTION {
        override fun icon() = R.drawable.baseline_cancel_24
    },
    CURRENT_SONG {
        override fun icon() = R.drawable.baseline_surround_sound_24
    };

    abstract fun icon(): Int
}