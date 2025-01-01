package com.example.backoffice_kelompok5.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.backoffice_kelompok5.HalamanPerCuti
import com.example.backoffice_kelompok5.R

class PercutiFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_percuti, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnSelanjutnya4: Button = view.findViewById(R.id.btn_selanjutnya4)
        btnSelanjutnya4.setOnClickListener {
            val intent = Intent(context, HalamanPerCuti::class.java)
            startActivity(intent)
        }
    }
}