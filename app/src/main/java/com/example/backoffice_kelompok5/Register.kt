package com.example.backoffice_kelompok5

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class Register: AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Referensi ke View
        val resNama = findViewById<EditText>(R.id.res_Nama)
        val resEmail = findViewById<EditText>(R.id.res_Email)
        val resPassword = findViewById<EditText>(R.id.res_Password)
        val resConfirmPassword = findViewById<EditText>(R.id.res_ConfirmPassword)
        val resDivisi = findViewById<Spinner>(R.id.res_Divisi)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        // Spinner Divisi
        val divisiOptions = listOf("IT", "Administrator", "Kepegawaian")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, divisiOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        resDivisi.adapter = adapter

        btnRegister.setOnClickListener {
            val nama = resNama.text.toString().trim()
            val email = resEmail.text.toString().trim()
            val password = resPassword.text.toString().trim()
            val confirmPassword = resConfirmPassword.text.toString().trim()
            val divisi = resDivisi.selectedItem.toString()

            if (nama.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || divisi.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Password dan Confirm Password tidak sesuai!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerUser(nama, email, password, divisi)
        }
    }

    private fun registerUser(nama: String, email: String, password: String, divisi: String) {
        // Role berdasarkan divisi
        val role = if (divisi == "IT" || divisi == "Administrator") "admin" else "user"

        // Generate ID pengguna secara manual
        val userId = database.child("auth").push().key ?: return

        // Buat objek auth
        val auth = Auth(userId, nama, email, password, divisi, role)

        // Simpan data ke Firebase Database
        database.child("auth").child(userId).setValue(auth)
            .addOnSuccessListener {
                Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                finish() // Tutup aktivitas
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan data ke database!", Toast.LENGTH_SHORT).show()
            }
    }
}
