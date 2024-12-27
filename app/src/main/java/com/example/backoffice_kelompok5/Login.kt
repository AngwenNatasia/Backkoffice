package com.example.backoffice_kelompok5

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.backoffice_kelompok5.databinding.ActivityLoginBinding
import com.google.firebase.database.*

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var database: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Referensi Firebase Database
        database = FirebaseDatabase.getInstance().reference.child("auth")
        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        database.orderByChild("email")
            .equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val storedPassword = userSnapshot.child("password").value.toString()
                            if (storedPassword == password) {
                                // Ambil data pengguna
                                val userId = userSnapshot.child("id").value.toString()
                                val nama = userSnapshot.child("nama").value.toString()
                                val divisi = userSnapshot.child("divisi").value.toString()
                                val role = userSnapshot.child("role").value.toString()

                                // Simpan sesi pengguna ke SharedPreferences
                                val editor = sharedPreferences.edit()
                                editor.putBoolean("isLoggedIn", true)
                                editor.putString("userId", userId) // Simpan id
                                editor.putString("nama", nama)
                                editor.putString("divisi", divisi)
                                editor.putString("role", role)
                                editor.apply()

                                // Tampilkan pesan dan pindah aktivitas sesuai role
                                Toast.makeText(this@Login, "Selamat datang, $nama dari divisi $divisi!", Toast.LENGTH_SHORT).show()
                                navigateBasedOnRole(role)
                                return
                            }
                        }
                        Toast.makeText(this@Login, "Password salah!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@Login, "Email tidak ditemukan!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Error: ${error.message}")
                    Toast.makeText(this@Login, "Gagal memproses login. Coba lagi nanti.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun navigateBasedOnRole(role: String) {
        when (role) {
            "admin" -> {
                val intent = Intent(this, Admin::class.java)
                startActivity(intent)
                finish()
            }
            "user" -> {
                val intent = Intent(this, MainActivityKaryawan::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
