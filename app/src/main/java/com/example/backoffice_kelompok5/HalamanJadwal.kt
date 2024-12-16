package com.example.backoffice_kelompok5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.content.Intent
import com.google.firebase.database.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HalamanJadwal : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbJadwal: DatabaseReference
    private lateinit var jadwalList: MutableList<Jadwal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_row)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        jadwalList = mutableListOf()
        dbJadwal = FirebaseDatabase.getInstance().getReference("jadwal")

        // Load data jadwal dari Firebase
        dbJadwal.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                jadwalList.clear()
                for (h in snapshot.children) {
                    val jadwal = h.getValue(Jadwal::class.java)
                    if (jadwal != null) {
                        jadwalList.add(jadwal)
                    }
                }
                // Update adapter dengan data baru
                recyclerView.adapter = JadwalAdapter(this@HalamanJadwal, jadwalList)
            }

            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(this@HalamanJadwal, "Gagal memuat data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tambah, menu) // Inflating menu dari resource
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_tambah -> {
                val intent = Intent(this, TambahJadwal::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}