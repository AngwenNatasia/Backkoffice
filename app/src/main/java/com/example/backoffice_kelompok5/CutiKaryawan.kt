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

class CutiKaryawan : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_cuti_karyawan, container, false)

        val nama = view.findViewById<EditText>(R.id.nama)
        val divisi = view.findViewById<EditText>(R.id.divisi)
        val status = view.findViewById<EditText>(R.id.status)
        val lamaCuti = view.findViewById<EditText>(R.id.lama_Cuti)
        val keterangan = view.findViewById<EditText>(R.id.keterangan)
        val tanggal = view.findViewById<EditText>(R.id.tanggal_cuti)
        val submitButton = view.findViewById<Button>(R.id.submit)

        val database = FirebaseDatabase.getInstance().reference

        submitButton.setOnClickListener {
            val namaKaryawan = nama.text.toString()
            val divisiKaryawan = divisi.text.toString()
            val statusKaryawan = status.text.toString()
            val lama = lamaCuti.text.toString()
            val tanggalCuti = tanggal.text.toString()
            val keteranganCuti = keterangan.text.toString()

            if (namaKaryawan.isNotEmpty() && divisiKaryawan.isNotEmpty() && keteranganCuti.isNotEmpty() && lama.isNotEmpty() && statusKaryawan.isNotEmpty() && tanggalCuti.isNotEmpty()) {

                val durasi = lama.toIntOrNull() ?: 0
                val cuti = Cuti(namaKaryawan, durasi, tanggalCuti, divisiKaryawan, keteranganCuti, statusKaryawan)

                val cutiId = database.child("cuti").push().key
                if (cutiId != null) {
                    database.child("cuti").child(cutiId).setValue(cuti)
                        .addOnSuccessListener {

                            Toast.makeText(requireContext(), "Cuti berhasil dicatat.", Toast.LENGTH_LONG).show()

                            nama.text.clear()
                            divisi.text.clear()
                            status.text.clear()
                            lamaCuti.text.clear()
                            keterangan.text.clear()
                            tanggal.text.clear()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Gagal mencatat cuti.", Toast.LENGTH_SHORT).show()
                        }
                }

            } else {
                Toast.makeText(requireContext(), "Harap isi semua field.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
