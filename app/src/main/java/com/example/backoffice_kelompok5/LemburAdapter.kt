package com.example.backoffice_kelompok5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter untuk RecyclerView
class LemburAdapter(private val lemburList: List<Lembur>) : RecyclerView.Adapter<LemburAdapter.LemburViewHolder>() {

    // ViewHolder yang berisi elemen-elemen tampilan
    class LemburViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaText: TextView = itemView.findViewById(R.id.tvNamaKaryawan)
        val divisiText: TextView = itemView.findViewById(R.id.tvDivisi)
        val lamaText: TextView = itemView.findViewById(R.id.tvLamaLembur)
        val tanggalText: TextView = itemView.findViewById(R.id.tvTanggalLembur)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LemburViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lembur, parent, false)
        return LemburViewHolder(view)
    }

    override fun onBindViewHolder(holder: LemburViewHolder, position: Int) {
        val lembur = lemburList[position]
        holder.namaText.text = lembur.nama
        holder.divisiText.text = lembur.divisi
        holder.lamaText.text = "Lama: ${lembur.lama} jam"
        holder.tanggalText.text = "Tanggal: ${lembur.tanggal}"
    }

    override fun getItemCount(): Int {
        return lemburList.size
    }
}
