package com.example.backoffice_kelompok5

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import java.util.*
import java.text.SimpleDateFormat

class TambahKaryawan: AppCompatActivity() {
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_karyawan)

        val nama = findViewById<Spinner>(R.id.in_nama)
        val divisi = findViewById<Spinner>(R.id.in_divisi)
        val jabatan = findViewById<EditText>(R.id.in_jabatan)
        val departemen = findViewById<EditText>(R.id.in_departemen)
        val jenisKelamin = findViewById<Spinner>(R.id.in_jenisKelamin)
        val noHP = findViewById<EditText>(R.id.in_noHP)
        val tglGabung = findViewById<EditText>(R.id.in_tglGabung)
        val simpanKywn = findViewById<Button>(R.id.simpanKywn)

        // Spinner Divisi
//        val divisiOptions = listOf("IT", "Administrator", "Kepegawaian")
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, divisiOptions)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        divisi.adapter = adapter

        // Spinner Jenis Kelamin
        val jenisKelaminOptions = listOf("Pria", "Wanita")
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, jenisKelaminOptions)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        jenisKelamin.adapter = adapter2

        // Referensi ke tabel "auth"
        dbRef = FirebaseDatabase.getInstance().getReference("auth")

        // Mengambil data nama dan divisi
        dbRef.get().addOnSuccessListener { snapshot ->
            val namaList = mutableListOf<String>()
            val divisiMap = mutableMapOf<String, String>()

            for (userSnapshot in snapshot.children) {
                val nama = userSnapshot.child("nama").value.toString()
                val divisi = userSnapshot.child("divisi").value.toString()

                namaList.add(nama)
                divisiMap[nama] = divisi // Pemetaan nama ke divisi
            }

            // Set data ke spinner nama
            val namaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, namaList)
            namaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            nama.adapter = namaAdapter

            // Update divisi spinner berdasarkan nama yang dipilih
            nama.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedNama = namaList[position]
                    val selectedDivisi = divisiMap[selectedNama] ?: ""

                    // Set divisi spinner dengan divisi yang sesuai
                    val divisiAdapter = ArrayAdapter(this@TambahKaryawan, android.R.layout.simple_spinner_item, listOf(selectedDivisi))
                    divisiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    divisi.adapter = divisiAdapter
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal mengambil data pengguna", Toast.LENGTH_SHORT).show()
        }

        simpanKywn.setOnClickListener {
            val nama = nama.selectedItem.toString()
            val divisi = divisi.selectedItem.toString()
            val jabatan = jabatan.text.toString().trim()
            val departemen = departemen.text.toString().trim()
            val jenisKelamin = jenisKelamin.selectedItem.toString()
            val noHP = noHP.text.toString().trim()

            val currentDate = Date()
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormatter.format(currentDate)
            tglGabung.setText(formattedDate)

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
                jenisKelamin = jenisKelamin,
                noHP = noHP,
                tglGabung = formattedDate
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