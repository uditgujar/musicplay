package com.example.musicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HindisongsActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var songAdapter: adapterHindisong
    private lateinit var songList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hindisongs)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Initialize RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.rvHindiSongList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        songList = mutableListOf()
        songAdapter = adapterHindisong(songList)
        recyclerView.adapter = songAdapter

        // Fetch songs from Firebase
        fetchBollywoodSongs()
    }

    private fun fetchBollywoodSongs() {
        database.child("categories").child("Hindi").addValueEventListener(object :
            ValueEventListener {
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
