package com.example.backoffice_kelompok5

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class UpdateKaryawan: AppCompatActivity() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var karyawanId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_karyawan)

        val upNama = findViewById<EditText>(R.id.up_nama)
        val upDivisi = findViewById<EditText>(R.id.up_divisi)
        val upJabatan = findViewById<EditText>(R.id.up_jabatan)
        val upDepartemen = findViewById<EditText>(R.id.up_departemen)
        val upJenisKelamin = findViewById<EditText>(R.id.up_jenisKelamin)
        val btnUpdate = findViewById<Button>(R.id.up_submitKywn)
        val btnDelete = findViewById<Button>(R.id.del_submitKywn)

        karyawanId = intent.getStringExtra("karyawanId") ?: ""

        dbRef = FirebaseDatabase.getInstance().getReference("karyawan")

        if (karyawanId.isNotEmpty()) {
            dbRef.child(karyawanId).get().addOnSuccessListener { snapshot ->
                val karyawan = snapshot.getValue(Karyawan::class.java)
                if (karyawan != null) {
                    upNama.setText(karyawan.nama)
                    upDivisi.setText(karyawan.divisi)
                    upJabatan.setText(karyawan.jabatan)
                    upDepartemen.setText(karyawan.departemen)
                    upJenisKelamin.setText(karyawan.jenisKelamin)
                } else {
                    Toast.makeText(this, "Karyawan tidak ditemukan", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        // Update Data
        btnUpdate.setOnClickListener {
            val updatedKaryawan = Karyawan(
                id = karyawanId,
                nama = upNama.text.toString().trim(),
                divisi = upDivisi.text.toString().trim(),
                jabatan = upJabatan.text.toString().trim(),
                departemen = upDepartemen.text.toString().trim(),
                jenisKelamin = upJenisKelamin.text.toString().trim()
            )

            dbRef.child(karyawanId).setValue(updatedKaryawan)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data karyawan berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show()
                }
        }

        // Delete Data
        btnDelete.setOnClickListener {
            dbRef.child(karyawanId).removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this, "Data karyawan berhasil dihapus", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
