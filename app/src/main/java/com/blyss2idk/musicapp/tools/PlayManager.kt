package com.blyss2idk.musicapp.tools

import android.media.AudioAttributes
import android.media.MediaPlayer
import com.blyss2idk.musicapp.data.Track

object PlayManager {

    // all time is in milliseconds
    private var crossfade = 0
    private var isPlaying = false
    private var offset = 0

    private var playerPlaying = 0
    private val mediaplayers = arrayOf(
        MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
        , MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
    )

    private var queue = ArrayList<Track>()


    fun getQueue(): ArrayList<Track> {
        return queue
    }

    fun setQueue(queue_: ArrayList<Track>) {
        queue = queue_
    }

    fun addQueue(track: Track) {
        queue.add(track)
    }

    fun removeQueue(index: Int) {
        queue.removeAt(index)
    }

    fun currentlyPlaying(): Boolean {
        return isPlaying
    }

    fun setCrossfade(crossfade_: Int) {
        crossfade = crossfade_
    }

    fun togglePlay() {
        if (isPlaying) {
            offset = mediaplayers[playerPlaying].currentPosition
            mediaplayers[playerPlaying].stop()
            isPlaying = false
        } else {
            mediaplayers[playerPlaying].apply {
                seekTo(offset)
                start()
            }
            isPlaying = true
        }
    }
}