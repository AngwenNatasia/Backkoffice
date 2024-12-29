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

class FormLemburActivity : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.form_lembur, container, false)

        val lamaLembur = view.findViewById<EditText>(R.id.lama_Lembur)
        val keterangan = view.findViewById<EditText>(R.id.keterangan_Lembur)
        val submitButton = view.findViewById<Button>(R.id.submit)

        val database = FirebaseDatabase.getInstance().reference

        submitButton.setOnClickListener {
            val lama = lamaLembur.text.toString().toIntOrNull() // Konversi ke Int
            val keteranganLembur = keterangan.text.toString()

            if (keteranganLembur.isNotEmpty() && lama != null && lama > 0) {
                // Objek data lembur
                val lembur = Lembur(
                    nama = "Nama User", // Ganti dengan data nama user yang sesuai
                    lama = lama, // Lama lembur sebagai Int
                    tanggal = "Tanggal Lembur", // Ganti dengan data tanggal yang sesuai
                    divisi = "Divisi User" // Ganti dengan data divisi yang sesuai
                )

                val lemburId = database.child("lembur").push().key
                if (lemburId != null) {
                    database.child("lembur").child(lemburId).setValue(lembur)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Lembur berhasil dicatat.", Toast.LENGTH_LONG).show()

                            lamaLembur.text.clear()
                            keterangan.text.clear()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Gagal mencatat lembur.", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(requireContext(), "Harap isi semua field dengan benar.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
