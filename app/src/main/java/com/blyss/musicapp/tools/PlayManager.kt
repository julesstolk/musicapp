package com.blyss.musicapp.tools

import android.content.ContentUris
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import com.blyss.musicapp.data.Track


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

    fun clearQueue() {
        queue.clear()
    }

    fun isCurrentlyPlaying(): Boolean {
        return mediaPlaying
    }

    fun currentTrackPlaying(): Track? {
        if (::currentlyPlaying.isInitialized) {
            return currentlyPlaying
        }
        return null
    }

    fun getCurrentTimeInSeconds(): Int {
        return mediaplayers[currentPlayer].currentPosition / 1000
    }

//    fun setCrossfade(crossfade_: Int) {
//        crossfade = crossfade_
//    }

    // Only changes mediaPlaying of all logic
    private fun directPlay(track: Track, position: Int, context: Context) {
        if (!track.validUri) {
            nextQueue(context)
        }
        if (mediaPlaying) {
            mediaPlaying = false
            mediaplayers[currentPlayer].pause()
        }

        val player = mediaplayers[currentPlayer]

        player.reset() // Always reset
        player.setDataSource(context, track.uri) // Always set data source after reset

        currentlyPlaying = track

        player.setOnPreparedListener {
            player.seekTo(position)
            player.start()
            mediaPlaying = true
        }

        player.setOnCompletionListener {
            nextQueue(context)
        }

        player.prepareAsync()
    }

    // Function is called when track ends:
    // Logic in this function, let directPlay() handle preparing and starting mediaplayer only
    // Except mediaPlaying!!!
    fun nextQueue(context: Context) {
        if (queue.isEmpty() && !::currentlyPlaying.isInitialized) {
            // no queue and no current track playing
            return
        }

        if (::currentlyPlaying.isInitialized) {
            history.add(currentlyPlaying)
        }

        if (queue.isNotEmpty()) {
            currentlyPlaying = queue.removeAt(0)
        }

        directPlay(currentlyPlaying, 0, context)
    }

    fun previousQueue(context: Context) {
        if (::currentlyPlaying.isInitialized) {
            queue.add(0, currentlyPlaying)
        }
        if (history.isNotEmpty()) {
            currentlyPlaying = history.removeAt(history.lastIndex)
        }
        directPlay(currentlyPlaying, 0, context)
    }

    fun startPlay(track: Track, context: Context) {
        directPlay(track, 0, context)
    }

    fun togglePlay(context: Context) {
        if (mediaPlaying) {
            // pause mediaplayer
            offset = mediaplayers[currentPlayer].currentPosition
            mediaplayers[currentPlayer].pause()
            mediaPlaying = false
        } else {
            mediaplayers[currentPlayer].apply {
                seekTo(offset)
                start()
            }
            mediaPlaying = true
        }
    }

//    fun fadeEnd(mp: MediaPlayer, duration: Int, start: Float, end: Float) {
//
//    }
}