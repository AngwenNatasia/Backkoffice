package com.example.backoffice_kelompok5.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.backoffice_kelompok5.HalamanIzinCuti
import com.example.backoffice_kelompok5.R

class IzinCutiFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_izin_cuti, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnSelanjutnya3: Button = view.findViewById(R.id.btn_selanjutnya3)
        btnSelanjutnya3.setOnClickListener {
            val intent = Intent(context, HalamanIzinCuti::class.java)
            intent.putExtra("kategori", "cuti")
            startActivity(intent)
        }
    }
}