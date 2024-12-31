package com.example.backoffice_kelompok5

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HalamanIzinCuti : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_izin_cuti)

        // Ambil data kategori dari Intent
        val kategori = intent.getStringExtra("kategori") ?: "izin"

        // Ubah title sesuai kategori
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        tvTitle.text = if (kategori == "izin") "Daftar Izin" else "Daftar Cuti"


        // Setup RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = IzinCutiAdapter(dataList, kategori)
    }
}
