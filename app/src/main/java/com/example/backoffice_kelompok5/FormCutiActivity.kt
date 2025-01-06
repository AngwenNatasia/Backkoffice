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

class FormCutiActivity : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.form_cuti, container, false)

        val lamaCuti = view.findViewById<EditText>(R.id.lama_Cuti)
        val keterangan = view.findViewById<EditText>(R.id.keterangan_Cuti)
        val submitButton = view.findViewById<Button>(R.id.submit)

        val database = FirebaseDatabase.getInstance().reference

        submitButton.setOnClickListener {
            val lama = lamaCuti.text.toString()
            val keteranganCuti = keterangan.text.toString()

            if (keteranganCuti.isNotEmpty() && lama.isNotEmpty()) {
                val durasi = lama.toIntOrNull() ?: 0

                val cuti = Cuti(
                    nama = "Nama User",
                    durasi = durasi,
                    tanggal = "Tanggal Cuti",
                    divisi = "Divisi User",
                    alasan = keteranganCuti
                )

                val cutiId = database.child("cuti").push().key
                if (cutiId != null) {
                    database.child("cuti").child(cutiId).setValue(cuti)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Cuti berhasil dicatat.", Toast.LENGTH_LONG).show()

                            lamaCuti.text.clear()
                            keterangan.text.clear()
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
