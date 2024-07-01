package com.example.musicplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class Splacescreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splacescreen)
        Handler().postDelayed({
            val intent=Intent(this@Splacescreen,Activity_HomePage::class.java)
            startActivity(intent)
            finish()
        },3000)


    }
}