package com.blyss.musicapp.tools

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.blyss.musicapp.R

private const val CHANNEL_ID = "playback_channel"
private const val NOTIFICATION_ID = 1

class PlaybackService : MediaSessionService() {

    private lateinit var player: ExoPlayer

    // private val customCommandFavorites = SessionCommand(ACTION_FAVORITES, Bundle.EMPTY)
    private lateinit var mediaSession: MediaSession

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        startForeground(NOTIFICATION_ID, buildPlaybackNotification())
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        // Register the channel BEFORE startForeground
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Media Playback",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Playback controls for music"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        player = PlayManager.getPlayer(applicationContext)

        mediaSession = MediaSession.Builder(this, player)
            .setCallback(MyCallback())
            .build()

        val notification = buildPlaybackNotification()
        startForeground(NOTIFICATION_ID, notification)
    }

    @OptIn(UnstableApi::class)
    private fun buildPlaybackNotification(): Notification {
        val title: String = if (PlayManager.currentTrackPlaying() == null) {
            "nothing playing"
        } else {
            PlayManager.currentTrackPlaying()!!.title
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_music_note_24)
            .setContentTitle(title)
            //.setContentText("Now playing...")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setStyle(androidx.media3.session.MediaStyleNotificationHelper.MediaStyle(mediaSession))
            .setOngoing(true)

        return builder.build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
        return mediaSession
    }

    private inner class MyCallback : MediaSession.Callback {
        @OptIn(UnstableApi::class)
        override fun onConnect(
            session: MediaSession,
            controller: MediaSession.ControllerInfo
        ): MediaSession.ConnectionResult {
            // Set available player and session commands.
            return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
                .setAvailableSessionCommands(
                    MediaSession.ConnectionResult.DEFAULT_SESSION_COMMANDS.buildUpon()
                        // .add(customCommandFavorites)
                        .build()
                )
                .build()
        }

//        override fun onCustomCommand(
//            session: MediaSession,
//            controller: MediaSession.ControllerInfo,
//            customCommand: SessionCommand,
//            args: Bundle
//        ): ListenableFuture {
//            if (customCommand.customAction == ACTION_FAVORITES) {
//                // Do custom logic here
//                saveToFavorites(session.player.currentMediaItem)
//                return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
//            }
//            return super.onCustomCommand(session, controller, customCommand, args)
//        }
    }
}