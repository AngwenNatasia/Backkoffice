package com.example.backoffice_kelompok5

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

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

        submitButton.setOnClickListener {
            val namaKaryawan = nama.text.toString()
            val divisiKaryawan = divisi.text.toString()
            val statusKaryawan = status.text.toString()
            val lama = lamaCuti.text.toString()
            val tanggal = tanggal.text.toString()
            val keterangan = keterangan.text.toString()

            if (namaKaryawan.isNotEmpty() && divisiKaryawan.isNotEmpty() && keterangan.isNotEmpty() && lama.isNotEmpty() && statusKaryawan.isNotEmpty() && tanggal.isNotEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "$namaKaryawan dari divisi $divisiKaryawan cuti karena $keterangan",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(requireContext(), "Harap isi semua field.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}