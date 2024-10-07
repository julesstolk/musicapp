package com.blyss2idk.musicapp.tools

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.blyss2idk.musicapp.data.Track

object PlayManager {

    // all time is in milliseconds
//    private var crossfade = 0
    private var currentlyPlaying: Track? = null
    private var offset = 0

    private var totalPlayers = 1
    private var currentPlayer = 0
    private val mediaplayers = Array(totalPlayers) {
        MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
    }

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
        return currentlyPlaying != null
    }

//    fun setCrossfade(crossfade_: Int) {
//        crossfade = crossfade_
//    }

    fun directPlay(track: Track, context: Context) {
        // TODO WIP
        if (track.uri != null) {
            if (currentlyPlaying != null) {
                mediaplayers[currentPlayer].apply {
                    stop()
                    setDataSource(context, track.uri)
                    setOnPreparedListener {
                        mediaplayers[currentPlayer].start()
                        currentlyPlaying = track
                    }
                    setOnCompletionListener {

                    }
                    prepareAsync()
                }
            }
        }
    }

    fun togglePlay() {
        if (currentlyPlaying != null) {
            offset = mediaplayers[currentPlayer].currentPosition
            mediaplayers[currentPlayer].stop()
        } else {
            // TODO THIS WILL NOT WORK
            mediaplayers[currentPlayer].apply {
                seekTo(offset)
                start()
            }
        }
    }

//    fun fadeEnd(mp: MediaPlayer, duration: Int, start: Float, end: Float) {
//
//    }
}