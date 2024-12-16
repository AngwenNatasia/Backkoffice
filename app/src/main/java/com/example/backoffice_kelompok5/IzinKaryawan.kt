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

class IzinKaryawan : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_izin_karyawan, container, false)

        val nama = view.findViewById<EditText>(R.id.nama)
        val divisi = view.findViewById<EditText>(R.id.divisi)
        val status = view.findViewById<EditText>(R.id.status)
        val lamaIzin = view.findViewById<EditText>(R.id.lama_izin)
        val keterangan = view.findViewById<EditText>(R.id.keterangan)
        val tanggalIzin = view.findViewById<EditText>(R.id.tanggal_izin)
        val submitButton = view.findViewById<Button>(R.id.submit)


        val database = FirebaseDatabase.getInstance().reference

        submitButton.setOnClickListener {
            val namaKaryawan = nama.text.toString()
            val divisiKaryawan = divisi.text.toString()
            val statusKaryawan = status.text.toString()
            val lama = lamaIzin.text.toString()
            val tanggal = tanggalIzin.text.toString()
            val keteranganIzin = keterangan.text.toString()


            if (namaKaryawan.isNotEmpty() && divisiKaryawan.isNotEmpty() && keteranganIzin.isNotEmpty() && lama.isNotEmpty() && statusKaryawan.isNotEmpty() && tanggal.isNotEmpty()) {

                val durasi = lama.toIntOrNull() ?: 0
                val izin = Izin(namaKaryawan, durasi, tanggal, divisiKaryawan, keteranganIzin, statusKaryawan)

                val izinId = database.child("izin").push().key
                if (izinId != null) {
                    database.child("izin").child(izinId).setValue(izin)
                        .addOnSuccessListener {

                            Toast.makeText(requireContext(), "Izin berhasil dicatat.", Toast.LENGTH_LONG).show()


                            nama.text.clear()
                            divisi.text.clear()
                            status.text.clear()
                            lamaIzin.text.clear()
                            keterangan.text.clear()
                            tanggalIzin.text.clear()
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
