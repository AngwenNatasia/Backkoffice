package com.example.backoffice_kelompok5

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class UpdateJadwal : AppCompatActivity() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var jadwalId: String
    private val karyawanNamaList = mutableListOf<String>()
    private lateinit var upNamaKywn: Spinner
    private lateinit var upHari: EditText
    private lateinit var upJamMasuk: EditText
    private lateinit var upJamKeluar: EditText
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_jadwal)

        upNamaKywn = findViewById(R.id.up_namaKywn)
        upHari = findViewById(R.id.up_hari)
        upJamMasuk = findViewById(R.id.up_jamMasuk)
        upJamKeluar = findViewById(R.id.up_jamKeluar)
        btnUpdate = findViewById(R.id.up_submitJadwal)
        btnDelete = findViewById(R.id.del_submitJadwal)

        dbRef = FirebaseDatabase.getInstance().getReference("jadwal")
        jadwalId = intent.getStringExtra("jadwalId") ?: ""

        if (jadwalId.isNotEmpty()) {
            fetchJadwalData()
        }

        fetchKaryawanData()

        btnUpdate.setOnClickListener {
            val selectedNama = upNamaKywn.selectedItem.toString().trim()
            val updatedJadwal = Jadwal(
                id = jadwalId,
                nama = selectedNama,
                hari = upHari.text.toString().trim(),
                jamMasuk = upJamMasuk.text.toString().trim(),
                jamKeluar = upJamKeluar.text.toString().trim()
            )

            dbRef.child(jadwalId).setValue(updatedJadwal)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data jadwal berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show()
                }
        }

        btnDelete.setOnClickListener {
            dbRef.child(jadwalId).removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this, "Data jadwal berhasil dihapus", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun fetchJadwalData() {
        dbRef.child(jadwalId).get().addOnSuccessListener { snapshot ->
            val jadwal = snapshot.getValue(Jadwal::class.java)
            if (jadwal != null) {
                upHari.setText(jadwal.hari)
                upJamMasuk.setText(jadwal.jamMasuk)
                upJamKeluar.setText(jadwal.jamKeluar)
                setSelectedNamaKaryawan(jadwal.nama)
            } else {
                Toast.makeText(this, "Jadwal tidak ditemukan", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchKaryawanData() {
        val karyawanRef = FirebaseDatabase.getInstance().getReference("karyawan")
        karyawanRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                karyawanNamaList.clear()
                for (data in snapshot.children) {
                    val karyawan = data.getValue(Karyawan::class.java)
                    if (karyawan != null) {
                        karyawanNamaList.add(karyawan.nama)
                    }
                }
                val adapter = ArrayAdapter(
                    this@UpdateJadwal,
                    android.R.layout.simple_spinner_item,
                    karyawanNamaList
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                upNamaKywn.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UpdateJadwal, "Gagal memuat data karyawan", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setSelectedNamaKaryawan(nama: String) {
        val index = karyawanNamaList.indexOf(nama)
        if (index >= 0) {
            upNamaKywn.setSelection(index)
        }
    }
}
