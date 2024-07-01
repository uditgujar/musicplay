package com.example.musicplayer

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
/*

class PlaylistAdapter(private val songs: List<Songdataclass>) : RecyclerView.Adapter<PlaylistAdapter.SongViewHolder>() {

    private var mediaPlayer: MediaPlayer? = null
    private var currentPlayingPosition: Int = -1
    private var isPlaying: Boolean = false

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songName: TextView = itemView.findViewById(R.id.song_name)
        val playPauseButton: ImageView = itemView.findViewById(R.id.playPauseButton)
        val heartButton: ImageView = itemView.findViewById(R.id.heartblack)

        fun bind(song: Songdataclass, position: Int) {
            songName.text = song.name

            if (currentPlayingPosition == position && isPlaying) {
                playPauseButton.setImageResource(R.drawable.pause__2)
            } else {
                playPauseButton.setImageResource(R.drawable.play)
            }

            playPauseButton.setOnClickListener {
                if (currentPlayingPosition != position) {
                    stopCurrentPlayingSong()
                    playSong(song.url, position)
                    playPauseButton.setImageResource(R.drawable.pause__2)
                } else {
                    if (isPlaying) {
                        pauseCurrentPlayingSong()
                        playPauseButton.setImageResource(R.drawable.play)
                    } else {
                        resumeCurrentPlayingSong()
                        playPauseButton.setImageResource(R.drawable.pause__2)
                    }
                }
            }

            heartButton.setOnClickListener {
                val isFavorite = it.tag as? Boolean ?: false
                if (isFavorite) {
                    heartButton.setImageResource(R.drawable.hearthd)
                    it.tag = false
                } else {
                    heartButton.setImageResource(R.drawable.greenheart)
                    it.tag = true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_homesonglist, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song, position)
    }

    override fun getItemCount(): Int = songs.size

    private fun playSong(url: String, position: Int) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            prepare()
            start()
            setOnCompletionListener {
                stopCurrentPlayingSong()
            }
        }
        currentPlayingPosition = position
        isPlaying = true
    }

    private fun stopCurrentPlayingSong() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        currentPlayingPosition = -1
        isPlaying = false
        notifyDataSetChanged()
    }

    private fun pauseCurrentPlayingSong() {
        mediaPlayer?.pause()
        isPlaying = false
        notifyDataSetChanged()
    }

    private fun resumeCurrentPlayingSong() {
        mediaPlayer?.start()
        isPlaying = true
        notifyDataSetChanged()
    }
}
*/



class PlaylistAdapter(private val songs: List<Songdataclass>, private val progressBar: ProgressBar) : RecyclerView.Adapter<PlaylistAdapter.SongViewHolder>() {

    private var mediaPlayer: MediaPlayer? = null
    private var currentPlayingPosition: Int = -1
    private var isPlaying: Boolean = false

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songName: TextView = itemView.findViewById(R.id.song_name)
        val playPauseButton: ImageView = itemView.findViewById(R.id.playPauseButton)
        val heartButton: ImageView = itemView.findViewById(R.id.heartblack)

        fun bind(song: Songdataclass, position: Int) {
            songName.text = song.name

            if (currentPlayingPosition == position && isPlaying) {
                playPauseButton.setImageResource(R.drawable.pause__2)
            } else {
                playPauseButton.setImageResource(R.drawable.play)
            }

            playPauseButton.setOnClickListener {
                if (currentPlayingPosition != position) {
                    stopCurrentPlayingSong()
                    playSong(song.url, position)
                    playPauseButton.setImageResource(R.drawable.pause__2)
                } else {
                    if (isPlaying) {
                        pauseCurrentPlayingSong()
                        playPauseButton.setImageResource(R.drawable.play)
                    } else {
                        resumeCurrentPlayingSong()
                        playPauseButton.setImageResource(R.drawable.pause__2)
                    }
                }
            }

            heartButton.setOnClickListener {
                val isFavorite = it.tag as? Boolean ?: false
                if (isFavorite) {
                    heartButton.setImageResource(R.drawable.hearthd)
                    it.tag = false
                } else {
                    heartButton.setImageResource(R.drawable.greenheart)
                    it.tag = true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_homesonglist, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song, position)
    }

    override fun getItemCount(): Int = songs.size

    private fun playSong(url: String, position: Int) {
        progressBar.visibility = View.VISIBLE

        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener {
                it.start()
                progressBar.visibility = View.GONE
            }
            setOnCompletionListener {
                stopCurrentPlayingSong()
            }
            setOnErrorListener { _, _, _ ->
                progressBar.visibility = View.GONE
                true
            }
        }
        currentPlayingPosition = position
        isPlaying = true
    }

    private fun stopCurrentPlayingSong() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        currentPlayingPosition = -1
        isPlaying = false
        notifyDataSetChanged()
    }

    private fun pauseCurrentPlayingSong() {
        mediaPlayer?.pause()
        isPlaying = false
        notifyDataSetChanged()
    }

    private fun resumeCurrentPlayingSong() {
        mediaPlayer?.start()
        isPlaying = true
        notifyDataSetChanged()
    }
}
