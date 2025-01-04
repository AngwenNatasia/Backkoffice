package com.example.backoffice_kelompok5

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class HalamanPerIzin : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var izinList: MutableList<Map<String, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_izin_cuti)

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inisialisasi database dan list izin
        izinList = mutableListOf()
        database = FirebaseDatabase.getInstance().getReference("izin")

        // Ambil data user dari SharedPreferences
        val sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", null)

        if (!userId.isNullOrEmpty()) {
//            // Ambil data izin dari Firebase untuk user terkait
//            database.orderByChild("nama").equalTo(userId)
//                .addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        izinList.clear()
//                        for (izinSnapshot in snapshot.children) {
//                            val izin = izinSnapshot.getValue(dataIzin)
//                            if (izin != null) {
//                                izinList.add(izin)
//                            }
//                        }
//
//                        // Pasang adapter ke RecyclerView
//                        recyclerView.adapter = PerIzinAdapter(this@HalamanPerIzin, izinList)
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        Toast.makeText(
//                            this@HalamanPerIzin,
//                            "Gagal memuat data izin: ${error.message}",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                })

            database.orderByChild("userId").equalTo(userId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        izinList.clear()
                        for (izinSnapshot in snapshot.children) {
                            // Ambil data langsung sebagai Map
                            val izinData = izinSnapshot.value as? Map<*, *>
                            if (izinData != null) {
                                val izin = mapOf(
                                    "userId" to (izinData["userId"] as? String ?: ""),
                                    "nama" to (izinData["nama"] as? String ?: ""),
                                    "divisi" to (izinData["divisi"] as? String ?: ""),
                                    "lamaIzin" to (izinData["lamaIzin"] as? String ?: ""),
                                    "tanggalIzin" to (izinData["tanggalIzin"] as? String ?: ""),
                                    "keterangan" to (izinData["keterangan"] as? String ?: "")
                                )
                                izinList.add(izin) // Tambahkan ke izinList
                            }
                        }
                        recyclerView.adapter = PerIzinAdapter(this@HalamanPerIzin, izinList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            this@HalamanPerIzin,
                            "Gagal memuat data izin: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })

        } else {
            Toast.makeText(this, "Sesi Anda telah berakhir. Silakan login kembali.", Toast.LENGTH_SHORT).show()
        }
    }
}
