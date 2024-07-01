package com.example.musicplayer

import android.app.*
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.media.MediaPlayer
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat

class MusicService : Service() {

    private val binder = MusicBinder()
    private var mediaPlayer: MediaPlayer? = null
    private var totalDuration: Int = 0
    private var isPrepared: Boolean = false

    companion object {
        const val ACTION_PLAY = "com.example.musicplayer.ACTION_PLAY"
        const val ACTION_PAUSE = "com.example.musicplayer.ACTION_PAUSE"
        const val ACTION_STOP = "com.example.musicplayer.ACTION_STOP"
        const val ACTION_MEDIA_PREPARED = "com.example.musicplayer.ACTION_MEDIA_PREPARED"
        const val CHANNEL_ID = "MusicPlayerChannel"
        const val NOTIFICATION_ID = 1
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> {
                val songUrl = intent.getStringExtra("SONG_URL")
                val songName = intent.getStringExtra("songName") ?: "Unknown Song"

                if (songUrl != null) {
                    play(songUrl, songName)
                }
            }
            ACTION_PAUSE -> pause()
            ACTION_STOP -> stop()
        }
        return START_NOT_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stop()
    }

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    fun play(songUrl: String, songName: String) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(songUrl)
            setOnPreparedListener {
                totalDuration = duration // Get total duration once prepared
                isPrepared = true
                sendMediaPreparedBroadcast(totalDuration) // Send total duration to activity
                startForegroundService()
                start()
                updateProgress()
            }
            prepareAsync()
        }
    }

    fun pause() {
        mediaPlayer?.pause()
        updateNotification("Paused")
    }

    private fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        stopForeground(true)
        stopSelf()
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    fun getTotalDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }

    private fun startForegroundService() {
        val notificationLayout = RemoteViews(packageName, R.layout.notification_custom)

        val notificationIntent = Intent(this, PlayerActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val playIntent = Intent(this, MusicService::class.java).apply { action = ACTION_PLAY }
        val playPendingIntent = PendingIntent.getService(
            this,
            0,
            playIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        notificationLayout.setOnClickPendingIntent(R.id.notification_play_pause, playPendingIntent)

        val pauseIntent = Intent(this, MusicService::class.java).apply { action = ACTION_PAUSE }
        val pausePendingIntent = PendingIntent.getService(
            this,
            0,
            pauseIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        notificationLayout.setOnClickPendingIntent(R.id.notification_play_pause, pausePendingIntent)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.spotifiy)
            .setContentIntent(pendingIntent)
            .setCustomContentView(notificationLayout)
            .setCustomBigContentView(notificationLayout)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Music Player Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun updateNotification(status: String) {
        val notificationLayout = RemoteViews(packageName, R.layout.notification_custom)

        val notificationIntent = Intent(this, PlayerActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.spotifiy)
            .setContentIntent(pendingIntent)
            .setCustomContentView(notificationLayout)
            .setCustomBigContentView(notificationLayout)
            .build()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun sendMediaPreparedBroadcast(duration: Int) {
        val intent = Intent(ACTION_MEDIA_PREPARED).apply {
            putExtra("totalDuration", duration)
        }
        sendBroadcast(intent)
    }

    private fun updateProgress() {
        Thread {
            while (mediaPlayer != null && isPrepared) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                    val currentPosition = mediaPlayer!!.currentPosition
                    val intent = Intent(ACTION_PLAY).apply {
                        putExtra("currentPosition", currentPosition)
                    }
                    sendBroadcast(intent)
                }
            }
        }.start()
    }
}
