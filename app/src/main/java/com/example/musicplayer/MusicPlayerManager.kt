package com.example.musicplayer

import android.content.Context
import android.content.Intent

object MusicPlayerManager {

    private var musicService: MusicService? = null

    fun setService(service: MusicService?) {
        musicService = service
    }

    fun play(context: Context, songUrl: String) {
        val intent = Intent(context, MusicService::class.java).apply {
            action = MusicService.ACTION_PLAY
            putExtra("SONG_URL", songUrl)
        }
        context.startService(intent)
    }

    fun pause() {
        musicService?.let {
            val intent = Intent(it, MusicService::class.java).apply {
                action = MusicService.ACTION_PAUSE
            }
            it.startService(intent)
        }
    }

    fun stop(context: Context) {
        val intent = Intent(context, MusicService::class.java).apply {
            action = MusicService.ACTION_STOP
        }
        context.startService(intent)
    }

    fun isPlaying(): Boolean {
        return musicService?.isPlaying() ?: false
    }

    fun getCurrentPosition(): Int {
        return musicService?.getCurrentPosition() ?: 0
    }

    fun getTotalDuration(): Int {
        return musicService?.getTotalDuration() ?: 0
    }

    fun seekTo(position: Int) {
        musicService?.seekTo(position)
    }
}
