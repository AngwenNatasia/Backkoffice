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

class IzinKaryawan : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_izin_karyawan)

        val nama = findViewById<EditText>(R.id.nama)
        val divisi = findViewById<EditText>(R.id.divisi)
        val lamaIzin = findViewById<EditText>(R.id.lama_izin)
        val keterangan = findViewById<EditText>(R.id.keterangan)
        val tanggalIzin = findViewById<EditText>(R.id.tanggal_izin)
        val submitButton = findViewById<Button>(R.id.submit)
        val backButton = findViewById<Button>(R.id.back_button)

        database = FirebaseDatabase.getInstance().reference
        val sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", null)


        if (!userId.isNullOrEmpty()) {
            database.child("auth").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val namaKaryawan = snapshot.child("nama").getValue(String::class.java) ?: ""
                        val divisiKaryawan = snapshot.child("divisi").getValue(String::class.java) ?: ""

                        nama.setText(namaKaryawan)
                        divisi.setText(divisiKaryawan)


                        nama.isEnabled = false
                        divisi.isEnabled = false
                    } else {
                        Toast.makeText(this@IzinKaryawan, "Data pengguna tidak ditemukan!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@IzinKaryawan, "Gagal memuat data: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Sesi Anda telah berakhir. Silakan login kembali.", Toast.LENGTH_SHORT).show()
        }


        submitButton.setOnClickListener {
            val lama = lamaIzin.text.toString()
            val tanggal = tanggalIzin.text.toString()
            val keteranganIzin = keterangan.text.toString()

            // Validasi input
            val dateRegex = Regex("\\d{2}-\\d{2}-\\d{4}")
            if (lama.toIntOrNull() == null || lama.toInt() <= 0) {
                Toast.makeText(this, "Durasi izin harus berupa angka positif.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!tanggal.matches(dateRegex)) {
                Toast.makeText(this, "Tanggal harus dalam format dd-MM-yyyy.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Kirim data ke database
            if (lama.isNotEmpty() && tanggal.isNotEmpty() && keteranganIzin.isNotEmpty()) {
                val izinId = database.child("izin").push().key
                if (izinId != null) {
                    val dataIzin = mapOf(
                        "nama" to nama.text.toString(),
                        "divisi" to divisi.text.toString(),
                        "lamaIzin" to lama,
                        "tanggalIzin" to tanggal,
                        "keterangan" to keteranganIzin
                    )

                    database.child("izin").child(izinId).setValue(dataIzin)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Izin berhasil dicatat.", Toast.LENGTH_LONG).show()


                            lamaIzin.text.clear()
                            tanggalIzin.text.clear()
                            keterangan.text.clear()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Gagal mencatat izin: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this, "Harap isi semua field.", Toast.LENGTH_SHORT).show()
            }
        }


        backButton.setOnClickListener {
            finish()
        }
    }
}
