package com.example.backoffice_kelompok5

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class TambahKaryawan: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_karyawan)

        val nama = findViewById<EditText>(R.id.in_nama)
        val divisi = findViewById<EditText>(R.id.in_divisi)
        val jabatan = findViewById<EditText>(R.id.in_jabatan)
        val departemen = findViewById<EditText>(R.id.in_departemen)
        val jenisKelamin = findViewById<EditText>(R.id.in_jenisKelamin)
        val simpanKywn = findViewById<Button>(R.id.simpanKywn)

        simpanKywn.setOnClickListener {
            val nama = nama.text.toString().trim()
            val divisi = divisi.text.toString().trim()
            val jabatan = jabatan.text.toString().trim()
            val departemen = departemen.text.toString().trim()
            val jenisKelamin = jenisKelamin.text.toString().trim()

            if (nama.isEmpty() or divisi.isEmpty() or jabatan.isEmpty() or departemen.isEmpty() or jenisKelamin.isEmpty()) {
                Toast.makeText(this, "Data tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val karyawanId = FirebaseDatabase.getInstance().getReference("karyawan").push().key

            val karyawan = Karyawan(
                id = karyawanId ?: "",
                nama = nama,
                divisi = divisi,
                jabatan = jabatan,
                departemen = departemen,
                jenisKelamin = jenisKelamin
            )

            // Simpan ke Firebase
            FirebaseDatabase.getInstance().getReference("karyawan")
                .child(karyawan.id)
                .setValue(karyawan)
                .addOnSuccessListener {
                    Toast.makeText(this, "Karyawan berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    finish() // Kembali ke activity sebelumnya
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal menambahkan karyawan", Toast.LENGTH_SHORT).show()
                }
        }
    }
}