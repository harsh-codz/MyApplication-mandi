package com.harshkethwas.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class registration2 : AppCompatActivity() {

    private lateinit var btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration2)

        btn = findViewById(R.id.RBtn)

        btn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

    }

}