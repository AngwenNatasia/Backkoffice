package com.example.backoffice_kelompok5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class IzinCutiAdapter(
    private val dataList: List<Pair<String, String>>,
    private val kategori: String
) : RecyclerView.Adapter<IzinCutiAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val namaTextView: TextView = view.findViewById(R.id.nama_kywn)
//        val alasanTextView: TextView = view.findViewById(if (kategori == "izin") R.id.ktgIzin else R.id.ktgCuti)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = if (kategori == "izin") {
            R.layout.list_data_izin
        } else {
            R.layout.list_data_cuti
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (nama, alasan) = dataList[position]
        holder.namaTextView.text = nama
//        holder.alasanTextView.text = alasan
    }

    override fun getItemCount(): Int = dataList.size
}
