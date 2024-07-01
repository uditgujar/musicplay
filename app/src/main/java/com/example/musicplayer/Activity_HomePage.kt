package com.example.musicplayer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.databinding.ActivityHomePageBinding
import com.google.firebase.database.*

/*
class Activity_HomePage : AppCompatActivity(), adapteruserphoto.OnItemClickListener {
    private lateinit var binding: ActivityHomePageBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Call the function to check and add categories and songs
        checkAndAddCategoriesAndSongs()

        // Set up singer photos RecyclerView
        setupSingerPhotosRecyclerView()

        // Set up Hindi songs RecyclerView
        setupHindiSongsRecyclerView()
    }

    private fun setupSingerPhotosRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.RVSingerphotos)

        // Sample data with 10 singers from India
        val sampleData = arrayListOf(
            dataclassuserphoto(R.drawable.honeysigha, "Honey Singh", "Volume 1"),
            dataclassuserphoto(R.drawable.arjitindia, "Arijit Singh", "Tum Hi Ho"),
            dataclassuserphoto(R.drawable.shreya_ghoshal, "Shreya Ghoshal", "Sun Raha Hai"),
            dataclassuserphoto(R.drawable.lata_mangeshkar, "Lata Mangeshkar", "Lag Ja Gale"),
            dataclassuserphoto(R.drawable.neha_kakkar, "Neha Kakkar", "Dilbar"),
            dataclassuserphoto(R.drawable.sidhu, "Sidhu Moose Wala", "So High")
        )

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapteruserphoto(this, sampleData, this)
    }




    private fun setupHindiSongsRecyclerView() {
        val playlistRecyclerView = findViewById<RecyclerView>(R.id.playlistsongs)
        playlistRecyclerView.layoutManager = LinearLayoutManager(this)

        database.child("categories").child("Hindi").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val songs = mutableListOf<Songdataclass>()
                for (songSnapshot in snapshot.children) {
                    val songUrl = songSnapshot.getValue(String::class.java) ?: ""
                    val songName = extractSongNameFromUrl(songUrl)
                    val song = Songdataclass(songName, songUrl)
                    songs.add(song)
                }
                playlistRecyclerView.adapter = PlaylistAdapter(songs)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Activity_HomePage", "Failed to read Hindi songs", error.toException())
            }
        })



}

    private fun checkAndAddCategoriesAndSongs() {
        val categories = mapOf(
            "bollywood" to listOf(
                "url_of_song_1",
                "url_of_song_2"
            ),
            "punjabi" to listOf(
                "url_of_song_1",
                "url_of_song_2"
            ),
            "south" to listOf(
                "url_of_song_1",
                "url_of_song_2"
            ),
            "english" to listOf(
                "url_of_song_1",
                "url_of_song_2"
            ),
            "hindi" to listOf(  // Add Hindi category
                "url_of_hindi_song_1",
                "url_of_hindi_song_2"
            )
        )

        database.child("categories").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    for ((category, songs) in categories) {
                        database.child("categories").child(category).setValue(songs)
                    }
                    Log.d("Activity_HomePage", "Categories and songs added to Firebase")
                } else {
                    Log.d("Activity_HomePage", "Categories already exist in Firebase")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Activity_HomePage", "Failed to read categories", error.toException())
            }
        })
    }

    override fun onItemClick(position: Int) {
        val intent = when (position) {
            0 -> Intent(this, Activity_SongList::class.java)
            1 -> Intent(this, PunjabiSongs::class.java)
            2 -> Intent(this, Activity_englishsongs::class.java)
            3 -> Intent(this, HindisongsActivity::class.java)
            4 -> Intent(this, Activity_SongList::class.java)
            5 -> Intent(this, PunjabiSongs::class.java)
            else -> Intent(this, Activity_SongList::class.java)
        }
        startActivity(intent)
    }

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
*/




class Activity_HomePage : AppCompatActivity(), adapteruserphoto.OnItemClickListener {
    private lateinit var binding: ActivityHomePageBinding
    private lateinit var database: DatabaseReference
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Initialize ProgressBar
        progressBar = this.findViewById(R.id.progressBar)

        // Call the function to check and add categories and songs
        checkAndAddCategoriesAndSongs()

        // Set up singer photos RecyclerView
        setupSingerPhotosRecyclerView()

        // Set up Hindi songs RecyclerView
        setupHindiSongsRecyclerView()
    }

    private fun setupSingerPhotosRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.RVSingerphotos)

        // Sample data with 10 singers from India
        val sampleData = arrayListOf(
            dataclassuserphoto(R.drawable.honeysigha, "Honey Singh", "Volume 1"),
            dataclassuserphoto(R.drawable.arjitindia, "Arijit Singh", "Tum Hi Ho"),
            dataclassuserphoto(R.drawable.shreya_ghoshal, "Shreya Ghoshal", "Sun Raha Hai"),
            dataclassuserphoto(R.drawable.lata_mangeshkar, "Lata Mangeshkar", "Lag Ja Gale"),
            dataclassuserphoto(R.drawable.neha_kakkar, "Neha Kakkar", "Dilbar"),
            dataclassuserphoto(R.drawable.sidhu, "Sidhu Moose Wala", "So High")
        )

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapteruserphoto(this, sampleData, this)
    }

    private fun setupHindiSongsRecyclerView() {
        val playlistRecyclerView = findViewById<RecyclerView>(R.id.playlistsongs)
        playlistRecyclerView.layoutManager = LinearLayoutManager(this)

        database.child("categories").child("Hindi").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val songs = mutableListOf<Songdataclass>()
                for (songSnapshot in snapshot.children) {
                    val songUrl = songSnapshot.getValue(String::class.java) ?: ""
                    val songName = extractSongNameFromUrl(songUrl)
                    val song = Songdataclass(songName, songUrl)
                    songs.add(song)
                }
                playlistRecyclerView.adapter = PlaylistAdapter(songs, progressBar)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Activity_HomePage", "Failed to read Hindi songs", error.toException())
            }
        })
    }

    private fun checkAndAddCategoriesAndSongs() {
        val categories = mapOf(
            "bollywood" to listOf(
                "url_of_song_1",
                "url_of_song_2"
            ),
            "punjabi" to listOf(
                "url_of_song_1",
                "url_of_song_2"
            ),
            "south" to listOf(
                "url_of_song_1",
                "url_of_song_2"
            ),
            "english" to listOf(
                "url_of_song_1",
                "url_of_song_2"
            ),
            "hindi" to listOf(  // Add Hindi category
                "url_of_hindi_song_1",
                "url_of_hindi_song_2"
            )
        )

        database.child("categories").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    for ((category, songs) in categories) {
                        database.child("categories").child(category).setValue(songs)
                    }
                    Log.d("Activity_HomePage", "Categories and songs added to Firebase")
                } else {
                    Log.d("Activity_HomePage", "Categories already exist in Firebase")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Activity_HomePage", "Failed to read categories", error.toException())
            }
        })
    }

    override fun onItemClick(position: Int) {
        val intent = when (position) {
            0 -> Intent(this, Activity_SongList::class.java)
            1 -> Intent(this, PunjabiSongs::class.java)
            2 -> Intent(this, Activity_englishsongs::class.java)
            3 -> Intent(this, HindisongsActivity::class.java)
            4 -> Intent(this, Activity_SongList::class.java)
            5 -> Intent(this, PunjabiSongs::class.java)
            else -> Intent(this, Activity_SongList::class.java)
        }
        startActivity(intent)
    }

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

    override fun onDestroy() {
        super.onDestroy()

    }
}
