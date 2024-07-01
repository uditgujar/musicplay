package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PlayerActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private lateinit var songNameTextView: TextView
    private lateinit var btnPause: ImageView
    private lateinit var btnForward: ImageView
    private lateinit var btnRewind: ImageView
    private lateinit var startTimeTextView: TextView
    private lateinit var endTimeTextView: TextView
    private lateinit var progressBar: SeekBar
    private lateinit var heartSave: ImageView
    private var isHeartSelected = false
    private var isTrackingTouch: Boolean = false
    private var totalDuration: Int = 0

    private lateinit var serviceConnection: ServiceConnection
    private lateinit var mediaPreparedReceiver: BroadcastReceiver
    private lateinit var progressReceiver: BroadcastReceiver
    private var musicService: MusicService? = null

    @SuppressLint("UnspecifiedRegisterReceiverFlag", "InlinedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        setContentView(R.layout.activity_player)

        songNameTextView = findViewById(R.id.songTitle)
        btnPause = findViewById(R.id.ic_pause)
        btnForward = findViewById(R.id.nextButton)
        btnRewind = findViewById(R.id.prevButton)
        startTimeTextView = findViewById(R.id.tv_current_time)
        endTimeTextView = findViewById(R.id.tv_total_time)
        progressBar = findViewById(R.id.seekbar_bar)
        heartSave = findViewById(R.id.heartsave)
       val back=findViewById<MaterialCardView>(R.id.Cvback)

        val songUrl = intent.getStringExtra("SONG_URL")
        val songName = intent.getStringExtra("SONG_NAME")

        back.setOnClickListener {
            finish()
        }


        songNameTextView.text = songName

        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as MusicService.MusicBinder
                musicService = binder.getService()
                MusicPlayerManager.setService(musicService)
                updateUI()
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                musicService = null
                MusicPlayerManager.setService(null)
            }
        }

        mediaPreparedReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {
                    if (it.action == MusicService.ACTION_MEDIA_PREPARED) {
                        totalDuration = it.getIntExtra("totalDuration", 0)
                        updateTotalTime()
                        btnPause.setImageResource(R.drawable.pauselogo)
                    }
                }
            }
        }

        progressReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {
                    if (it.action == MusicService.ACTION_PLAY) {
                        val currentPosition = it.getIntExtra("currentPosition", 0)
                        updateCurrentTime(currentPosition)
                    }
                }
            }
        }

        bindService(Intent(this, MusicService::class.java), serviceConnection, Context.BIND_AUTO_CREATE)
        registerReceiver(mediaPreparedReceiver, IntentFilter(MusicService.ACTION_MEDIA_PREPARED), Context.RECEIVER_NOT_EXPORTED)
        registerReceiver(progressReceiver, IntentFilter(MusicService.ACTION_PLAY), Context.RECEIVER_NOT_EXPORTED)

        songUrl?.let {
            progressBar.progress = 0 // Reset progress bar to 0
            MusicPlayerManager.play(this, it)
        }

        btnPause.setOnClickListener {
            if (MusicPlayerManager.isPlaying()) {
                MusicPlayerManager.pause()
                btnPause.setImageResource(R.drawable.stop)
            } else {
                songUrl?.let { it1 -> MusicPlayerManager.play(this, it1) }
                btnPause.setImageResource(R.drawable.pauselogo)
            }
        }

        btnForward.setOnClickListener {
            seekForward()
        }

        btnRewind.setOnClickListener {
            seekBackward()
        }

        heartSave.setOnClickListener {
            isHeartSelected = !isHeartSelected
            if (isHeartSelected) {
                heartSave.setImageResource(R.drawable.greenheart) // Replace with your green heart drawable
            } else {
                heartSave.setImageResource(R.drawable.heartlogo) // Replace with your black heart drawable
            }
        }

        updateSeekBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
        unregisterReceiver(mediaPreparedReceiver)
        unregisterReceiver(progressReceiver)
        job.cancel()
    }

    private fun updateCurrentTime(currentPosition: Int) {
        startTimeTextView.text = formatTime(currentPosition)
        progressBar.progress = currentPosition
    }

    private fun updateTotalTime() {
        endTimeTextView.text = formatTime(totalDuration)
        progressBar.max = totalDuration
    }

    private fun updateSeekBar() {
        progressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    startTimeTextView.text = formatTime(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isTrackingTouch = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isTrackingTouch = false
                MusicPlayerManager.seekTo(progressBar.progress)
            }
        })
    }

    private fun formatTime(ms: Int): String {
        val seconds = ms / 1000
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

    private fun seekForward() {
        val currentPosition = MusicPlayerManager.getCurrentPosition()
        val newPosition = currentPosition + 5000 // 5000 milliseconds = 5 seconds
        MusicPlayerManager.seekTo(newPosition.coerceAtMost(totalDuration))
        progressBar.progress = newPosition.coerceAtMost(totalDuration)
    }

    private fun seekBackward() {
        val currentPosition = MusicPlayerManager.getCurrentPosition()
        val newPosition = currentPosition - 5000 // 5000 milliseconds = 5 seconds
        MusicPlayerManager.seekTo(newPosition.coerceAtLeast(0))
        progressBar.progress = newPosition.coerceAtLeast(0)
    }

    private fun updateUI() {
        launch {
            while (isActive) {
                delay(1000) // Update every second
                if (!isTrackingTouch && MusicPlayerManager.isPlaying()) {
                    val currentPosition = MusicPlayerManager.getCurrentPosition()
                    updateCurrentTime(currentPosition)
                }
                // Update play/pause button image based on playback state
                if (MusicPlayerManager.isPlaying()) {
                    btnPause.setImageResource(R.drawable.pauselogo)
                } else {
                    btnPause.setImageResource(R.drawable.stop)
                }
            }
        }
    }
}
