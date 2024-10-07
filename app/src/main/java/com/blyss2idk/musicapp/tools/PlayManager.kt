package com.blyss2idk.musicapp.tools

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.blyss2idk.musicapp.data.Track


// - hardcoded on no crossfade rn
object PlayManager {

    // all time is in milliseconds
//    private var crossfade = 0
    private var mediaPlaying: Boolean = false
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

    private lateinit var currentlyPlaying: Track
    private var queue = ArrayList<Track>()
    private var history = ArrayList<Track>()


    fun getQueue(): ArrayList<Track> {
        return queue
    }

    fun setQueue(newQueue: ArrayList<Track>) {
        queue = newQueue
    }

    fun addQueue(track: Track) {
        queue.add(track)
    }

    fun removeQueue(index: Int) {
        queue.removeAt(index)
    }

    fun currentlyPlaying(): Boolean {
        return mediaPlaying
    }

//    fun setCrossfade(crossfade_: Int) {
//        crossfade = crossfade_
//    }

    // Only changes mediaPlaying of all logic
    fun directPlay(track: Track, position: Int, context: Context) {
        if (mediaPlaying) {
            mediaPlaying = false
            mediaplayers[currentPlayer].stop()
        }
        if (currentlyPlaying != track) {
            mediaplayers[currentPlayer].apply {
                reset()
                setDataSource(context, track.uri)
            }
        }
        mediaplayers[currentPlayer].apply {
            setOnPreparedListener {
                mediaplayers[currentPlayer].apply {
                    seekTo(position)
                    start()
                }
                mediaPlaying = true
            }
            setOnCompletionListener {
                nextQueue(context)
            }
            prepareAsync()
        }
    }

    // Function is called when track ends:
    // Logic in this function, let directPlay() handle preparing and starting mediaplayer only
    // Except mediaPlaying!!!
    fun nextQueue(context: Context) {
        history.add(currentlyPlaying)
        currentlyPlaying = queue.removeAt(0)
        directPlay(currentlyPlaying, 0, context)
    }

    fun previousQueue(context: Context) {
        queue.add(0, currentlyPlaying)
        currentlyPlaying = history.removeLast()
        directPlay(currentlyPlaying, 0, context)
    }

    fun togglePlay(context: Context) {
        if (mediaPlaying) {
            // pause mediaplayer
            offset = mediaplayers[currentPlayer].currentPosition
            mediaplayers[currentPlayer].stop()
        } else {
            directPlay(currentlyPlaying, offset, context)
        }
    }

//    fun fadeEnd(mp: MediaPlayer, duration: Int, start: Float, end: Float) {
//
//    }
}