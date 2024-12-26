package com.example.backoffice_kelompok5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import android.content.Intent
import com.google.firebase.database.*

class Login : AppCompatActivity(){
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Referensi ke View
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // Firebase Database instance
        database = FirebaseDatabase.getInstance().reference

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Password harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        database.child("auth")
            .orderByChild("email")
            .equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val auth = userSnapshot.getValue(Auth::class.java)

                            if (auth != null && auth.password == password) {
                                handleRole(auth)
                                return
                            }
                        }
                        Toast.makeText(this@Login, "Password salah!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@Login, "Email tidak ditemukan!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@Login, "Gagal memproses login: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun handleRole(auth: Auth) {
        when (auth.role) {
            "admin" -> {
                Toast.makeText(this, "Selamat datang Admin ${auth.nama}!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Admin::class.java)
                startActivity(intent)
            }
            "user" -> {
                Toast.makeText(this, "Selamat datang ${auth.nama}!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivityKaryawan::class.java)
                startActivity(intent)
            }
//            else -> {
//                Toast.makeText(this, "Role tidak valid!", Toast.LENGTH_SHORT).show()
//            }
        }
    }
}