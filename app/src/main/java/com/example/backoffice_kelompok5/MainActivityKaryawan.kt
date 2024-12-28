package com.example.backoffice_kelompok5

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivityKaryawan : AppCompatActivity() {
    private lateinit var tvWelcome: TextView
    private lateinit var tvSchedule: TextView
    private lateinit var btnHadir: Button
    private lateinit var database: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_karyawan)

        tvWelcome = findViewById(R.id.tv_welcome)
        tvSchedule = findViewById(R.id.tv_schedule)
        btnHadir = findViewById(R.id.btn_hadir)
        sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)

        // Inisialisasi Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Tampilkan nama dan jadwal pengguna
        displayUserName()
        displayUserSchedule()

        // Tombol "Hadir" untuk mencatat presensi
        btnHadir.setOnClickListener {
            markAttendance()
        }
    }

    private fun displayUserName() {
        val userId = sharedPreferences.getString("userId", null)
        if (userId.isNullOrEmpty()) {
            tvWelcome.text = "Selamat Datang, User"
            return
        }

        database.child("auth").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val nama = snapshot.child("nama").getValue(String::class.java) ?: "User"
                    tvWelcome.text = "Selamat Datang, $nama"
                } else {
                    tvWelcome.text = "Selamat Datang, User"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                tvWelcome.text = "Selamat Datang, User"
                Toast.makeText(
                    this@MainActivityKaryawan,
                    "Gagal memuat data: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun displayUserSchedule() {
        val userId = sharedPreferences.getString("userId", null)
        if (userId.isNullOrEmpty()) {
            tvSchedule.text = "Schedule: Tidak tersedia"
            return
        }

        // Ambil nama pengguna dari Firebase
        database.child("auth").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Ambil nama pengguna
                    val nama = snapshot.child("nama").getValue(String::class.java) ?: "User"

                    // Sekarang ambil jadwal berdasarkan nama (tanpa menampilkan nama)
                    database.child("jadwal").orderByChild("nama").equalTo(nama)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(jadwalSnapshot: DataSnapshot) {
                                if (jadwalSnapshot.exists()) {
                                    // Ambil jadwal pertama yang ditemukan dengan nama yang cocok
                                    val jadwal = jadwalSnapshot.children.firstOrNull()?.getValue(Jadwal::class.java)
                                    if (jadwal != null) {
                                        // Menampilkan jadwal (tanpa menampilkan nama)
                                        tvSchedule.text = "Jadwal: ${jadwal.hari}, ${jadwal.jamMasuk} - ${jadwal.jamKeluar}"
                                    } else {
                                        tvSchedule.text = "Schedule: Tidak tersedia"
                                    }
                                } else {
                                    tvSchedule.text = "Schedule: Tidak tersedia"
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                tvSchedule.text = "Schedule: Tidak tersedia"
                                Toast.makeText(
                                    this@MainActivityKaryawan,
                                    "Gagal memuat jadwal: ${error.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                } else {
                    tvSchedule.text = "Schedule: Tidak tersedia"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                tvSchedule.text = "Schedule: Tidak tersedia"
                Toast.makeText(
                    this@MainActivityKaryawan,
                    "Gagal memuat data: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    private fun markAttendance() {
        val userId = sharedPreferences.getString("userId", null)
        if (userId.isNullOrEmpty()) {
            Toast.makeText(this, "User ID tidak ditemukan!", Toast.LENGTH_SHORT).show()
            return
        }

        database.child("auth").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val nama = snapshot.child("nama").getValue(String::class.java) ?: "User"
                    val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

                    // Simpan ke tabel presensi
                    val presensiData = mapOf(
                        "nama" to nama,
                        "timestamp" to timestamp
                    )

                    database.child("presensi").push().setValue(presensiData)
                        .addOnSuccessListener {
                            Toast.makeText(this@MainActivityKaryawan, "Presensi berhasil dicatat!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@MainActivityKaryawan, "Gagal mencatat presensi!", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this@MainActivityKaryawan, "Data pengguna tidak ditemukan!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivityKaryawan, "Gagal memuat data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
