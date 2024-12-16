package com.example.backoffice_kelompok5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.content.Intent
import com.google.firebase.database.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HalamanManajemenData: AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbKaryawan: DatabaseReference
    private lateinit var karyawanList: MutableList<Karyawan>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_row)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        karyawanList = mutableListOf()
        dbKaryawan = FirebaseDatabase.getInstance().getReference("karyawan")

        dbKaryawan.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                karyawanList.clear()
                for (h in snapshot.children) {
                    val karyawan = h.getValue(Karyawan::class.java)
                    if (karyawan != null) {
                        karyawanList.add(karyawan)
                    }
                }
                recyclerView.adapter = KaryawanAdapter(this@HalamanManajemenData, karyawanList)
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tambah, menu) // Ganti dengan nama file menu Anda
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_tambah -> {
                // Ganti dengan intent ke activity yang diinginkan
                val intent = Intent(this, TambahKaryawan::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}