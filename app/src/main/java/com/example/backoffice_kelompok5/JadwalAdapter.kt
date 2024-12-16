package com.example.backoffice_kelompok5

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class JadwalAdapter(
    private val jadwalContext: Context,
    private val jadwalList: List<Jadwal>
) : RecyclerView.Adapter<JadwalAdapter.JadwalViewHolder>() {

    class JadwalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaKaryawan: TextView = itemView.findViewById(R.id.nama_kywn)
        val jamMasuk: TextView = itemView.findViewById(R.id.jamMasuk)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JadwalViewHolder {
        val view = LayoutInflater.from(jadwalContext).inflate(R.layout.list_data_jadwal, parent, false)
        return JadwalViewHolder(view)
    }

    override fun onBindViewHolder(holder: JadwalViewHolder, position: Int) {
        val jadwal = jadwalList[position]
        holder.namaKaryawan.text = jadwal.nama
        holder.jamMasuk.text = "${jadwal.jamMasuk} - ${jadwal.jamKeluar}"

        holder.itemView.setOnClickListener {
            val intent = Intent(jadwalContext, UpdateJadwal::class.java)
            intent.putExtra("jadwalId", jadwal.id) // Mengirim ID jadwal
            jadwalContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return jadwalList.size
    }
}