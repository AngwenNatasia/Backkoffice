package com.example.backoffice_kelompok5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnAdmin = findViewById<Button>(R.id.btnAdmin)
        val btnKaryawan = findViewById<Button>(R.id.btnKaryawan)

        btnAdmin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        btnKaryawan.setOnClickListener {
            val intent = Intent(this, MainActivityKaryawan::class.java)
            startActivity(intent)
        }
    }
}

