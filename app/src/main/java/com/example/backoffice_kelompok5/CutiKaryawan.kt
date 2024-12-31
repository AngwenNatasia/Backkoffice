package com.example.backoffice_kelompok5

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class CutiKaryawan : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_cuti_karyawan)

        val nama = findViewById<EditText>(R.id.nama)
        val divisi = findViewById<EditText>(R.id.divisi)
        val lamaCuti = findViewById<EditText>(R.id.lama_cuti)
        val keterangan = findViewById<EditText>(R.id.keterangan)
        val tanggalCuti = findViewById<EditText>(R.id.tanggal_cuti)
        val submitButton = findViewById<Button>(R.id.submit)
        val backButton = findViewById<Button>(R.id.back)

        database = FirebaseDatabase.getInstance().reference
        val sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", null)


        if (!userId.isNullOrEmpty()) {
            database.child("auth").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val namaKaryawan = snapshot.child("nama").getValue(String::class.java) ?: ""
                        val divisiKaryawan = snapshot.child("divisi").getValue(String::class.java) ?: ""

                        nama.setText(namaKaryawan)
                        divisi.setText(divisiKaryawan)


                        nama.isEnabled = false
                        divisi.isEnabled = false
                    } else {
                        Toast.makeText(this@CutiKaryawan, "Data pengguna tidak ditemukan!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@CutiKaryawan, "Gagal memuat data: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Sesi Anda telah berakhir. Silakan login kembali.", Toast.LENGTH_SHORT).show()
        }


        submitButton.setOnClickListener {
            val lama = lamaCuti.text.toString()
            val tanggal = tanggalCuti.text.toString()
            val keteranganCuti = keterangan.text.toString()


            if (lama.isEmpty()) {
                Toast.makeText(this, "Durasi cuti tidak boleh kosong.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date: Date? = try {
                dateFormat.parse(tanggal)
            } catch (e: Exception) {
                null
            }

            if (date == null) {
                Toast.makeText(this, "Tanggal harus dalam format yyyy-MM-dd.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if (lama.isNotEmpty() && tanggal.isNotEmpty() && keteranganCuti.isNotEmpty()) {
                val cutiId = database.child("cuti").push().key
                if (cutiId != null) {
                    val dataCuti = mapOf(
                        "nama" to nama.text.toString(),
                        "divisi" to divisi.text.toString(),
                        "lamaCuti" to lama,
                        "tanggalCuti" to tanggal,
                        "keterangan" to keteranganCuti
                    )

                    database.child("cuti").child(cutiId).setValue(dataCuti)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Cuti berhasil dicatat.", Toast.LENGTH_LONG).show()


                            lamaCuti.text.clear()
                            tanggalCuti.text.clear()
                            keterangan.text.clear()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Gagal mencatat cuti: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this, "Harap isi semua field.", Toast.LENGTH_SHORT).show()
            }
        }


        backButton.setOnClickListener {
            finish()
        }
    }
}
