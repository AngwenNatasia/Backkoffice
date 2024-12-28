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

class FormIzinActivity : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.form_izin, container, false)

        val lamaIzin = view.findViewById<EditText>(R.id.lama_Izin)
        val keterangan = view.findViewById<EditText>(R.id.keterangan_Izin)
        val submitButton = view.findViewById<Button>(R.id.submit)

        val database = FirebaseDatabase.getInstance().reference

        submitButton.setOnClickListener {
            val lama = lamaIzin.text.toString()
            val keteranganIzin = keterangan.text.toString()

            if (keteranganIzin.isNotEmpty() && lama.isNotEmpty()) {
                val durasi = lama.toIntOrNull() ?: 0

                val izin = Izin(
                    nama = "Nama User",
                    durasi = durasi,
                    tanggal = "Tanggal Izin",
                    divisi = "Divisi User",
                    alasan = keteranganIzin,
                    status = "Pending"
                )

                val izinId = database.child("izin").push().key
                if (izinId != null) {
                    database.child("izin").child(izinId).setValue(izin)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Izin berhasil dicatat.", Toast.LENGTH_LONG).show()

                            lamaIzin.text.clear()
                            keterangan.text.clear()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Gagal mencatat izin.", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(requireContext(), "Harap isi semua field.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
