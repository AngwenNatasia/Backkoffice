package com.example.backoffice_kelompok5.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.backoffice_kelompok5.R
import android.widget.Button
import android.content.Intent
import com.example.backoffice_kelompok5.HalamanManajemenData

class ManajemenDataFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manajemen_data, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnSelanjutnya: Button = view.findViewById(R.id.btn_selanjutnya)
        btnSelanjutnya.setOnClickListener {
            val intent = Intent(context, HalamanManajemenData::class.java)
            startActivity(intent)
        }
    }
}
