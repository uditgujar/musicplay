package com.example.musicplayer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class Activity_SongList : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var songAdapter: SongAdapter
    private lateinit var songList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_list)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Initialize RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewSongList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        songList = mutableListOf()
        songAdapter = SongAdapter(songList)
        recyclerView.adapter = songAdapter

        // Fetch songs from Firebase
        fetchBollywoodSongs()
    }

    private fun fetchBollywoodSongs() {
        database.child("categories").child("bollywood").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Activity_SongList", "onDataChange called")
                songList.clear()
                for (songSnapshot in snapshot.children) {
                    val songUrl = songSnapshot.getValue(String::class.java) ?: ""
                    Log.d("Activity_SongList", "Song URL: $songUrl")
                    songList.add(songUrl)
                }
                songAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Activity_SongList", "Failed to read songs", error.toException())
            }
        })
    }
}
