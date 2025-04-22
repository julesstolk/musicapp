package com.blyss.musicapp.tools

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.blyss.musicapp.data.Playlist
import com.blyss.musicapp.data.Track


// - hardcoded on no crossfade rn
object PlayManager {

    // all time is in milliseconds
    private lateinit var exoPlayer: ExoPlayer
    private var mediaPlaying: Boolean = false
    private var offset: Long = 0L

    private lateinit var currentlyPlaying: Track
    private var queue = ArrayList<Track>()
    private var history = ArrayList<Track>()

    private var mediaSessionStarted = false

    fun initPlayer(context: Context) {
        exoPlayer = ExoPlayer.Builder(context).build()
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    nextQueue(context)
                }
            }
        })
    }

    fun getPlayer(context: Context): ExoPlayer {
        if (!::exoPlayer.isInitialized) {
            initPlayer(context)
        }
        return exoPlayer
    }

    fun release() {
        exoPlayer.release()
    }

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
        return (exoPlayer.currentPosition / 1000).toInt()
    }

//    fun setCrossfade(crossfade_: Int) {
//        crossfade = crossfade_
//    }

    // Only changes mediaPlaying of all logic
    private fun directPlay(track: Track, position: Long, context: Context) {
        if (!track.validUri) {
            nextQueue(context)
            return
        }

        if (::currentlyPlaying.isInitialized) {
            mediaPlaying = false
            exoPlayer.pause()
        }

        currentlyPlaying = track

        val mediaItem = MediaItem.fromUri(track.uri)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.seekTo(position)
        exoPlayer.play()
        mediaPlaying = true
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
            currentlyPlaying = history.removeAt(history.size - 1)
        }

        directPlay(currentlyPlaying, 0, context)
    }

    fun startPlay(track: Track, context: Context) {
        if (!mediaSessionStarted) {
            startMediaSession(context)
            mediaSessionStarted = true
        }
        directPlay(track, 0, context)
    }

    fun startPlay(playlist: Playlist, context: Context) {
        if (playlist.size() == 0) {
            return
        }

        val tracks = playlist.getTracks()
        startPlay(tracks[0], context)

        if (playlist.getTracks().size > 1) {
            queue.addAll(tracks.subList(1, tracks.size))
        }
    }

    fun togglePlay() {
        if (mediaPlaying) {
            // pause mediaplayer
            offset = exoPlayer.currentPosition
            exoPlayer.pause()
            mediaPlaying = false
        } else {
            exoPlayer.seekTo(offset)
            exoPlayer.play()
            mediaPlaying = true
        }
    }

    // for the notification to work
    fun startMediaSession(context: Context) {
        val intent = Intent(context, PlaybackService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

//    fun fadeEnd(mp: MediaPlayer, duration: Int, start: Float, end: Float) {
//
//    }
}