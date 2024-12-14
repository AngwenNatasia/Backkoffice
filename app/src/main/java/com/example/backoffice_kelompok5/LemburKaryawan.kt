package com.example.backoffice_kelompok5

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.FirebaseDatabase

class LemburKaryawan : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_lembur_karyawan, container, false)

        val nama = view.findViewById<EditText>(R.id.nama)
        val divisi = view.findViewById<EditText>(R.id.divisi)
        val status = view.findViewById<EditText>(R.id.status)
        val lamaLembur = view.findViewById<EditText>(R.id.lama_lembur)
        val tanggalHadir = view.findViewById<EditText>(R.id.tanggal_lembur)
        val submitButton = view.findViewById<Button>(R.id.submit)

        // Referensi ke Firebase Realtime Database
        val database = FirebaseDatabase.getInstance().reference

        submitButton.setOnClickListener {
            val namaKaryawan = nama.text.toString()
            val divisiKaryawan = divisi.text.toString()
            val statusKaryawan = status.text.toString()
            val lama = lamaLembur.text.toString()
            val tanggal = tanggalHadir.text.toString()

            // Validasi inputan
            if (namaKaryawan.isNotEmpty() && divisiKaryawan.isNotEmpty() && lama.isNotEmpty() && statusKaryawan.isNotEmpty() && tanggal.isNotEmpty()) {

                // Membuat objek Lembur
                val durasi = lama.toIntOrNull() ?: 0 // Jika lama lembur tidak valid, set 0
                val lembur = Lembur(namaKaryawan, durasi, tanggal, divisiKaryawan, statusKaryawan)

                // Menyimpan data ke Firebase Database
                val lemburId = database.child("lembur").push().key // Membuat ID unik untuk lembur
                if (lemburId != null) {
                    database.child("lembur").child(lemburId).setValue(lembur)
                        .addOnSuccessListener {
                            // Menampilkan Toast sukses
                            Toast.makeText(requireContext(), "Lembur berhasil dicatat.", Toast.LENGTH_LONG).show()

                            // Mengosongkan field input setelah data tersimpan
                            nama.text.clear()
                            divisi.text.clear()
                            status.text.clear()
                            lamaLembur.text.clear()
                            tanggalHadir.text.clear()
                        }
                        .addOnFailureListener {
                            // Menampilkan Toast gagal
                            Toast.makeText(requireContext(), "Gagal mencatat lembur.", Toast.LENGTH_SHORT).show()
                        }
                }

            } else {
                // Menampilkan Toast jika ada field yang kosong
                Toast.makeText(requireContext(), "Harap isi semua field.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
