package com.example.backoffice_kelompok5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.content.Intent

class Login : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val password = etPassword.text.toString()
            if (password == "admin") {
                Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Admin::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Password salah", Toast.LENGTH_SHORT).show()
            }
        }
    }
}