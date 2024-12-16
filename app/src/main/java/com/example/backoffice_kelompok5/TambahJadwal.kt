package com.example.backoffice_kelompok5

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class TambahJadwal: AppCompatActivity() {
    private val karyawanIdList = mutableListOf<String>()
    private val karyawanNamaList = mutableListOf<String>()
    private lateinit var in_namaKywn: Spinner
    private lateinit var in_hari: EditText
    private lateinit var in_jamMasuk: EditText
    private lateinit var in_jamKeluar: EditText
    private lateinit var submitJadwal: Button
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_jadwal)

        in_namaKywn = findViewById(R.id.in_namaKywn)
        in_hari = findViewById(R.id.in_hari)
        in_jamMasuk = findViewById(R.id.in_jamMasuk)
        in_jamKeluar = findViewById(R.id.in_jamKeluar)
        submitJadwal = findViewById(R.id.submitJadwal)

        db = FirebaseDatabase.getInstance().getReference()

        fetchKaryawanData()

        submitJadwal.setOnClickListener {
            val hari = in_hari.text.toString().trim()
            val jamMasuk = in_jamMasuk.text.toString().trim()
            val jamKeluar = in_jamKeluar.text.toString().trim()

            if (hari.isEmpty() or jamMasuk.isEmpty() or jamKeluar.isEmpty()) {
                Toast.makeText(this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedIndex = in_namaKywn.selectedItemPosition
            if (selectedIndex < 0 || selectedIndex >= karyawanNamaList.size) {
                Toast.makeText(this, "Pilih karyawan terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedNamaKaryawan = karyawanNamaList[selectedIndex]
            val jadwalId = db.child("jadwal").push().key
            if (jadwalId == null) {
                Toast.makeText(this, "Gagal membuat ID jadwal", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val jadwal = Jadwal(jadwalId, selectedNamaKaryawan, hari, jamMasuk, jamKeluar)

            db.child("jadwal").child(jadwalId).setValue(jadwal)
                .addOnSuccessListener {
                    Toast.makeText(this, "Jadwal berhasil disimpan!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal menyimpan jadwal!", Toast.LENGTH_SHORT).show()
                }
        }
    }
    private fun fetchKaryawanData() {
        val karyawanRef = db.child("karyawan")

        karyawanRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                karyawanIdList.clear()
                karyawanNamaList.clear()

                for (data in snapshot.children) {
                    val karyawan = data.getValue(Karyawan::class.java)
                    if (karyawan != null) {
                        karyawanIdList.add(karyawan.id)
                        karyawanNamaList.add(karyawan.nama)
                    }
                }

                if (karyawanNamaList.isEmpty()) {
                    Toast.makeText(this@TambahJadwal, "Tidak ada data karyawan ditemukan", Toast.LENGTH_SHORT).show()
                }

                // Set nama karyawan ke Spinner
                val adapter = ArrayAdapter(
                    this@TambahJadwal,
                    android.R.layout.simple_spinner_item,
                    karyawanNamaList
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                in_namaKywn.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@TambahJadwal, "Gagal memuat data karyawan", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
