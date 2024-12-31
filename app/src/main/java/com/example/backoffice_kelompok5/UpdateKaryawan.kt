//package com.example.backoffice_kelompok5
//
//import android.os.Bundle
//import android.view.View
//import android.widget.*
//import androidx.appcompat.app.AppCompatActivity
//import com.google.firebase.database.*
//import java.util.*
//
//class UpdateKaryawan: AppCompatActivity() {
//    private lateinit var dbRef: DatabaseReference
//    private lateinit var karyawanId: String
//    private lateinit var tglGabung: String
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_update_karyawan)
//
//        val upNama = findViewById<Spinner>(R.id.up_nama)
//        val upDivisi = findViewById<Spinner>(R.id.up_divisi)
//        val upJabatan = findViewById<EditText>(R.id.up_jabatan)
//        val upDepartemen = findViewById<EditText>(R.id.up_departemen)
//        val upJenisKelamin = findViewById<Spinner>(R.id.up_jenisKelamin)
//        val upNoHp = findViewById<EditText>(R.id.up_noHP)
//        val upTglGabung = findViewById<EditText>(R.id.up_tglGabung)
//        val btnUpdate = findViewById<Button>(R.id.up_submitKywn)
//        val btnDelete = findViewById<Button>(R.id.del_submitKywn)
//
//        // Spinner Divisi
//        val divisiOptions = listOf("IT", "Administrator", "Kepegawaian")
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, divisiOptions)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        upDivisi.adapter = adapter
//
//        // Spinner Jenis Kelamin
//        val jenisKelaminOptions = listOf("Pria", "Wanita")
//        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, jenisKelaminOptions)
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        upJenisKelamin.adapter = adapter2
//
//        karyawanId = intent.getStringExtra("karyawanId") ?: ""
//
//        dbRef = FirebaseDatabase.getInstance().getReference("karyawan")
//
//        if (karyawanId.isNotEmpty()) {
//            dbRef.child(karyawanId).get().addOnSuccessListener { snapshot ->
//                val karyawan = snapshot.getValue(Karyawan::class.java)
//                if (karyawan != null) {
//                    upNama.setText(karyawan.nama)
//                    upDivisi.setSelection(divisiOptions.indexOf(karyawan.divisi))
//                    upJabatan.setText(karyawan.jabatan)
//                    upDepartemen.setText(karyawan.departemen)
//                    upJenisKelamin.setSelection(jenisKelaminOptions.indexOf(karyawan.jenisKelamin))
//                    upNoHp.setText(karyawan.noHP)
//                    tglGabung = karyawan.tglGabung
//                    upTglGabung.setText(tglGabung)
//
//                } else {
//                    Toast.makeText(this, "Karyawan tidak ditemukan", Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//            }.addOnFailureListener {
//                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
//                finish()
//            }
//        }
//
//        // Update Data
//        btnUpdate.setOnClickListener {
//            val updatedKaryawan = Karyawan(
//                id = karyawanId,
//                nama = upNama.text.toString().trim(),
//                divisi = upDivisi.selectedItem.toString(),
//                jabatan = upJabatan.text.toString().trim(),
//                departemen = upDepartemen.text.toString().trim(),
//                jenisKelamin = upJenisKelamin.selectedItem.toString(),
//                noHP = upNoHp.toString().trim(),
//                tglGabung = tglGabung
//            )
//
//            dbRef.child(karyawanId).setValue(updatedKaryawan)
//                .addOnSuccessListener {
//                    Toast.makeText(this, "Data karyawan berhasil diperbarui", Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//                .addOnFailureListener {
//                    Toast.makeText(this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show()
//                }
//        }
//
//        // Delete Data
//        btnDelete.setOnClickListener {
//            dbRef.child(karyawanId).removeValue()
//                .addOnSuccessListener {
//                    Toast.makeText(this, "Data karyawan berhasil dihapus", Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//                .addOnFailureListener {
//                    Toast.makeText(this, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
//                }
//        }
//    }
//}


package com.example.backoffice_kelompok5

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class UpdateKaryawan : AppCompatActivity() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var authRef: DatabaseReference
    private lateinit var karyawanId: String
    private lateinit var tglGabung: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_karyawan)

        val upNama = findViewById<Spinner>(R.id.up_nama)
        val upDivisi = findViewById<Spinner>(R.id.up_divisi)
        val upJabatan = findViewById<EditText>(R.id.up_jabatan)
        val upDepartemen = findViewById<EditText>(R.id.up_departemen)
        val upJenisKelamin = findViewById<Spinner>(R.id.up_jenisKelamin)
        val upNoHp = findViewById<EditText>(R.id.up_noHP)
        val upTglGabung = findViewById<EditText>(R.id.up_tglGabung)
        val btnUpdate = findViewById<Button>(R.id.up_submitKywn)
        val btnDelete = findViewById<Button>(R.id.del_submitKywn)

        // Spinner Jenis Kelamin
        val jenisKelaminOptions = listOf("Pria", "Wanita")
        val adapterJenisKelamin = ArrayAdapter(this, android.R.layout.simple_spinner_item, jenisKelaminOptions)
        adapterJenisKelamin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        upJenisKelamin.adapter = adapterJenisKelamin

        // Ambil ID karyawan dari Intent
        karyawanId = intent.getStringExtra("karyawanId") ?: ""

        // Referensi ke tabel
        dbRef = FirebaseDatabase.getInstance().getReference("karyawan")
        authRef = FirebaseDatabase.getInstance().getReference("auth")

        // Ambil data dari tabel "auth" untuk Spinner nama
        authRef.get().addOnSuccessListener { snapshot ->
            val namaList = mutableListOf<String>()
            val divisiMap = mutableMapOf<String, String>()

            for (userSnapshot in snapshot.children) {
                val nama = userSnapshot.child("nama").value.toString()
                val divisi = userSnapshot.child("divisi").value.toString()
                namaList.add(nama)
                divisiMap[nama] = divisi
            }

            val namaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, namaList)
            namaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            upNama.adapter = namaAdapter

            // Atur data karyawan untuk di-update
            if (karyawanId.isNotEmpty()) {
                dbRef.child(karyawanId).get().addOnSuccessListener { snapshot ->
                    val karyawan = snapshot.getValue(Karyawan::class.java)
                    if (karyawan != null) {
                        upNama.setSelection(namaList.indexOf(karyawan.nama))
                        upDivisi.setSelection(
                            namaList.indexOf(karyawan.nama).takeIf { it >= 0 } ?: 0
                        )
                        upJabatan.setText(karyawan.jabatan)
                        upDepartemen.setText(karyawan.departemen)
                        upJenisKelamin.setSelection(jenisKelaminOptions.indexOf(karyawan.jenisKelamin))
                        upNoHp.setText(karyawan.noHP)
                        tglGabung = karyawan.tglGabung
                        upTglGabung.setText(tglGabung)
                    } else {
                        Toast.makeText(this, "Karyawan tidak ditemukan", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            // Update divisi berdasarkan nama yang dipilih
            upNama.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedNama = namaList[position]
                    val selectedDivisi = divisiMap[selectedNama] ?: ""
                    val divisiAdapter = ArrayAdapter(
                        this@UpdateKaryawan,
                        android.R.layout.simple_spinner_item,
                        listOf(selectedDivisi)
                    )
                    divisiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    upDivisi.adapter = divisiAdapter
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal mengambil data pengguna", Toast.LENGTH_SHORT).show()
        }

        // Update Data
        btnUpdate.setOnClickListener {
            val updatedKaryawan = Karyawan(
                id = karyawanId,
                nama = upNama.selectedItem.toString(),
                divisi = upDivisi.selectedItem.toString(),
                jabatan = upJabatan.text.toString().trim(),
                departemen = upDepartemen.text.toString().trim(),
                jenisKelamin = upJenisKelamin.selectedItem.toString(),
                noHP = upNoHp.text.toString().trim(),
                tglGabung = tglGabung
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
