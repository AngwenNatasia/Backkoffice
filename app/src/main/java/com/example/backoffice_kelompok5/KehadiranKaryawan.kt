package com.example.backoffice_kelompok5

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class KehadiranKaryawan : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_kehadiran_karyawan, container, false)

        val nama = view.findViewById<EditText>(R.id.nama)
        val divisi = view.findViewById<EditText>(R.id.divisi)
        val status = view.findViewById<EditText>(R.id.status)
        val tanggalHadir = view.findViewById<EditText>(R.id.tanggal_hadir)
        val submitButton = view.findViewById<Button>(R.id.submit)

        submitButton.setOnClickListener {
            val namaKaryawan = nama.text.toString()
            val divisiKaryawan = divisi.text.toString()
            val statusKaryawan = status.text.toString()
            val tanggal = tanggalHadir.text.toString()

            if (namaKaryawan.isNotEmpty() && divisiKaryawan.isNotEmpty() && statusKaryawan.isNotEmpty() && tanggal.isNotEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Presensi $namaKaryawan dari divisi $divisiKaryawan dengan status $statusKaryawan pada tanggal $tanggal telah dicatat.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(requireContext(), "Harap isi semua field.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}