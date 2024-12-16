package com.example.backoffice_kelompok5

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.content.Intent
import de.hdodenhof.circleimageview.CircleImageView
import androidx.recyclerview.widget.RecyclerView

class KaryawanAdapter(
    val karyawanContext: Context,
    val karyawanList: List<Karyawan>
    ): RecyclerView.Adapter<KaryawanAdapter.KaryawanViewHolder>() {

    class KaryawanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gambarKaryawan: CircleImageView = itemView.findViewById(R.id.gambar_karyawan)
        val namaKaryawan: TextView = itemView.findViewById(R.id.nama_karyawan)
        val divisiKaryawan: TextView = itemView.findViewById(R.id.divisi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KaryawanViewHolder {
        val view = LayoutInflater.from(karyawanContext).inflate(R.layout.list_data_karyawan, parent, false)
        return KaryawanViewHolder(view)
    }

    override fun onBindViewHolder(holder: KaryawanViewHolder, position: Int) {
        val karyawan = karyawanList[position]
        holder.namaKaryawan.text = karyawan.nama
        holder.divisiKaryawan.text = karyawan.divisi
        // Glide.with(context).load(karyawan.imageUrl).into(holder.gambarKaryawan)

        holder.itemView.setOnClickListener {
            val intent = Intent(karyawanContext, UpdateKaryawan::class.java)
            intent.putExtra("karyawanId", karyawan.id) // Mengirim ID karyawan
            karyawanContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return karyawanList.size
    }
}
