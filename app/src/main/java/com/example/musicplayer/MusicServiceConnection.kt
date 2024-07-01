package com.example.musicplayer


import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder


class MusicServiceConnection(private val context: Context) : ServiceConnection {

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MusicBinder
        val musicService = binder.getService()
        MusicPlayerManager.setService(musicService)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        MusicPlayerManager.setService(null)
    }
}
