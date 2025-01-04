package com.example.backoffice_kelompok5

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

//class PerIzinAdapter(
//    private val izinContext: Context,
//    private val izinList: MutableList<Izin>,
//    private val approvalList: MutableList<Approval>
//) : RecyclerView.Adapter<PerIzinAdapter.IzinViewHolder>() {
//
//    private val dbRefApproval: DatabaseReference = FirebaseDatabase.getInstance().getReference("approval")
//
//    class IzinViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val namaTextView: TextView = view.findViewById(R.id.nama_kywn)
//        val alasanTextView: TextView = view.findViewById(R.id.ktgIzin)
//        val statusTextView: TextView = view.findViewById(R.id.statusIzin)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IzinViewHolder {
//        val view = LayoutInflater.from(izinContext).inflate(R.layout.list_data_izin, parent, false)
//        return IzinViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: IzinViewHolder, position: Int) {
//        val izin = izinList[position]
//        holder.namaTextView.text = izin.nama.trim()
//        holder.alasanTextView.text = "Alasan: ${izin.alasan.trim()}"
//
//        // Cari status persetujuan untuk data cuti
//        val approval = approvalList.find { it.nama == izin.nama }
//        if (approval != null) {
//            holder.statusTextView.visibility = View.VISIBLE
//            holder.statusTextView.text = "Status: ${approval.status}"
//        } else {
//            holder.statusTextView.visibility = View.INVISIBLE
//        }
//
//        holder.itemView.setOnClickListener {
//            showConfirmationDialog(izin, position, holder)
//        }
//    }
//    override fun getItemCount(): Int = izinList.size
//
//    private fun showConfirmationDialog(izin: Izin, position: Int, holder: IzinViewHolder) {
//        val builder = AlertDialog.Builder(izinContext)
//        builder.setTitle("Konfirmasi Status")
//        builder.setMessage("Apakah Anda ingin menerima atau menolak permintaan izin ini?")
//        builder.setPositiveButton("Terima") { _, _ ->
//            updateStatus(izin, "Diterima", position, holder)
//        }
//        builder.setNegativeButton("Tolak") { _, _ ->
//            updateStatus(izin, "Ditolak", position, holder)
//        }
//        builder.setNeutralButton("Batal") { dialog, _ ->
//            dialog.dismiss()
//        }
//        builder.show()
//    }
//
//    private fun updateStatus(izin: Izin, status: String, position: Int, holder: IzinViewHolder) {
//        val approvalData = Approval(
//            nama = izin.nama.trim(),
//            jenis = "Izin",
//            alasan = izin.alasan.trim(),
//            status = status
//        )
//
//        // Simpan data ke tabel approval di Firebase
//        dbRefApproval.child(approvalData.nama).setValue(approvalData)
//            .addOnSuccessListener {
//                val existingIndex = approvalList.indexOfFirst { it.nama.trim() == izin.nama.trim() }
//                if (existingIndex != -1) {
//                    // Ganti item lama dengan objek baru
//                    approvalList[existingIndex] = approvalData
//                } else {
//                    // Tambahkan objek baru jika belum ada
//                    approvalList.add(approvalData)
//                }
//
//                // Update status di tampilan
//                holder.statusTextView.visibility = View.VISIBLE
//                holder.statusTextView.text = "Status: $status"
//
//                notifyItemChanged(position)
//                Toast.makeText(izinContext, "Status berhasil diperbarui", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener {
//                Toast.makeText(izinContext, "Gagal memperbarui status", Toast.LENGTH_SHORT).show()
//            }
//    }
//}

//class PerIzinAdapter(
//    private val izinContext: Context,
//    private val izinList: List<Izin>
//) : RecyclerView.Adapter<PerIzinAdapter.IzinViewHolder>() {
//
//    class IzinViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val namaTextView: TextView = view.findViewById(R.id.nama_kywn)
//        val alasanTextView: TextView = view.findViewById(R.id.ktgIzin)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IzinViewHolder {
//        val view = LayoutInflater.from(izinContext).inflate(R.layout.list_data_izin, parent, false)
//        return IzinViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: IzinViewHolder, position: Int) {
//        val izin = izinList[position]
//        holder.namaTextView.text = izin.nama.trim()
//        holder.alasanTextView.text = "Alasan: ${izin.alasan.trim()}"
//
//        holder.itemView.setOnClickListener {
//            Toast.makeText(izinContext, "Klik pada ${izin.nama.trim()}", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    override fun getItemCount(): Int = izinList.size
//}

class PerIzinAdapter(
    private val izinContext: Context,
    private val izinList: List<Map<String, String>>
) : RecyclerView.Adapter<PerIzinAdapter.IzinViewHolder>() {

    class IzinViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val namaTextView: TextView = view.findViewById(R.id.nama_kywn)
        val alasanTextView: TextView = view.findViewById(R.id.ktgIzin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IzinViewHolder {
        val view = LayoutInflater.from(izinContext).inflate(R.layout.list_data_izin, parent, false)
        return IzinViewHolder(view)
    }

    override fun onBindViewHolder(holder: IzinViewHolder, position: Int) {
        val izin = izinList[position]
        holder.namaTextView.text = izin["nama"] ?: "Tidak Ada Nama"
        holder.alasanTextView.text = "Alasan: ${izin["keterangan"] ?: "Tidak Ada Keterangan"}"

        holder.itemView.setOnClickListener {
            Toast.makeText(izinContext, "Klik pada ${izin["nama"]}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return izinList.size
    }
}


