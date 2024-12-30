package com.example.backoffice_kelompok5

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LemburKaryawan : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_lembur_karyawan) // Pastikan nama layout XML sesuai

        val nama = findViewById<EditText>(R.id.nama)
        val divisi = findViewById<EditText>(R.id.divisi)
        val lamaLembur = findViewById<EditText>(R.id.lama_lembur)
        val tanggalLembur = findViewById<EditText>(R.id.tanggal_lembur)
        val submitButton = findViewById<Button>(R.id.submit)
        val backButton = findViewById<Button>(R.id.back)

        database = FirebaseDatabase.getInstance().reference
        val sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", null)

        // Ambil data karyawan (nama dan divisi) dari Firebase dan set ke EditText
        if (!userId.isNullOrEmpty()) {
            database.child("auth").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val namaKaryawan = snapshot.child("nama").getValue(String::class.java) ?: ""
                        val divisiKaryawan = snapshot.child("divisi").getValue(String::class.java) ?: ""

                        nama.setText(namaKaryawan)
                        divisi.setText(divisiKaryawan)

                        // Nonaktifkan pengeditan
                        nama.isEnabled = false
                        divisi.isEnabled = false
                    } else {
                        Toast.makeText(this@LemburKaryawan, "Data pengguna tidak ditemukan!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@LemburKaryawan, "Gagal memuat data: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Sesi Anda telah berakhir. Silakan login kembali.", Toast.LENGTH_SHORT).show()
        }

        // Logika tombol Submit
        submitButton.setOnClickListener {
            val lama = lamaLembur.text.toString()
            val tanggal = tanggalLembur.text.toString()

            // Validasi input
            val dateRegex = Regex("\\d{2}-\\d{2}-\\d{4}")
            if (lama.toIntOrNull() == null || lama.toInt() <= 0) {
                Toast.makeText(this, "Durasi lembur harus berupa angka positif.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!tanggal.matches(dateRegex)) {
                Toast.makeText(this, "Tanggal harus dalam format dd-MM-yyyy.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Kirim data ke database
            if (lama.isNotEmpty() && tanggal.isNotEmpty()) {
                val lemburId = database.child("lembur").push().key
                if (lemburId != null) {
                    val dataLembur = mapOf(
                        "nama" to nama.text.toString(),
                        "divisi" to divisi.text.toString(),
                        "lamaLembur" to lama,
                        "tanggalLembur" to tanggal
                    )

                    database.child("lembur").child(lemburId).setValue(dataLembur)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Lembur berhasil dicatat.", Toast.LENGTH_LONG).show()

                            // Kosongkan form kecuali nama dan divisi
                            lamaLembur.text.clear()
                            tanggalLembur.text.clear()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Gagal mencatat lembur: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this, "Harap isi semua field.", Toast.LENGTH_SHORT).show()
            }
        }

        // Logika tombol Back
        backButton.setOnClickListener {
            finish() // Menutup aktivitas saat ini dan kembali ke MainActivity
        }
    }
}
