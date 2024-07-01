package com.example.musicplayer

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class adapterPunjabi(private val songList: List<String>) : RecyclerView.Adapter<adapterPunjabi.SongViewHolder>() {

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songId: TextView = itemView.findViewById(R.id.songTitle)
        val songName: TextView = itemView.findViewById(R.id.songArtist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.song_list_item_recycler_row, parent, false)
        return SongViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val currentSongUrl = songList[position]
        val songId = (position + 1).toString()
        val songName = extractSongNameFromUrl(currentSongUrl)

        holder.songId.text = songId
        holder.songName.text = songName

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, PlayerActivity::class.java).apply {
                putExtra("SONG_NAME", songName)
                putExtra("SONG_URL", currentSongUrl)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = songList.size

    private fun extractSongNameFromUrl(url: String): String {
        var name = url.substringAfterLast('/').substringBeforeLast('.')
        name = name.replace(Regex("[0-9%]"), "")
        name = name.replace(Regex("(?i)(pagalworld\\.com|\\.sb)"), "")
        name = name.replace(Regex("(\\p{Lower})(\\p{Upper})"), "$1 $2")
        name = name.replace(Regex("[()]"), "").trim()
        name = name.removePrefix("Songs").trim()
        name = name.removePrefix("F").trim()
        name = name.replace("_", "")
        return name.trim()
    }
}
