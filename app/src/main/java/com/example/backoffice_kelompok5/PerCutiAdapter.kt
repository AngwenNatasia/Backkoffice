package com.example.backoffice_kelompok5

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class PerCutiAdapter(
    private val cutiContext: Context,
    private val cutiList: MutableList<Cuti>,
    private val approvalList: MutableList<Approval>
) : RecyclerView.Adapter<PerCutiAdapter.CutiViewHolder>() {

    private val dbRefApproval: DatabaseReference = FirebaseDatabase.getInstance().getReference("approval")

    class CutiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val namaTextView: TextView = view.findViewById(R.id.nama_kywn)
        val alasanTextView: TextView = view.findViewById(R.id.ktgCuti)
        val statusTextView: TextView = view.findViewById(R.id.statusCuti)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CutiViewHolder {
        val view = LayoutInflater.from(cutiContext).inflate(R.layout.list_data_cuti, parent, false)
        return CutiViewHolder(view)
    }

    override fun onBindViewHolder(holder: CutiViewHolder, position: Int) {
        val cuti = cutiList[position]
        holder.namaTextView.text = cuti.nama
        holder.alasanTextView.text = cuti.alasan

        // Cari status persetujuan untuk data cuti
        val approval = approvalList.find { it.nama == cuti.nama }
        if (approval != null) {
            holder.statusTextView.visibility = View.VISIBLE
            holder.statusTextView.text = "Status: ${approval.status}"
        } else {
            holder.statusTextView.visibility = View.INVISIBLE
        }

        holder.itemView.setOnClickListener {
            showConfirmationDialog(cuti, position, holder)
        }
    }

    override fun getItemCount(): Int = cutiList.size

    private fun showConfirmationDialog(cuti: Cuti, position: Int, holder: CutiViewHolder) {
        val builder = AlertDialog.Builder(cutiContext)
        builder.setTitle("Konfirmasi Status")
        builder.setMessage("Apakah Anda ingin menerima atau menolak permintaan cuti ini?")
        builder.setPositiveButton("Terima") { _, _ ->
            updateStatus(cuti, "Diterima", position, holder)
        }
        builder.setNegativeButton("Tolak") { _, _ ->
            updateStatus(cuti, "Ditolak", position, holder)
        }
        builder.setNeutralButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun updateStatus(cuti: Cuti, status: String, position: Int, holder: CutiViewHolder) {
        val approvalData = Approval(
            nama = cuti.nama,
            jenis = "Cuti",
            alasan = cuti.alasan,
            status = status
        )

        // Simpan data ke tabel approval di Firebase
        dbRefApproval.child(approvalData.nama).setValue(approvalData)
            .addOnSuccessListener {
                val existingIndex = approvalList.indexOfFirst { it.nama == cuti.nama }
                if (existingIndex != -1) {
                    // Ganti item lama dengan objek baru
                    approvalList[existingIndex] = approvalData
                } else {
                    // Tambahkan objek baru jika belum ada
                    approvalList.add(approvalData)
                }

                // Update status di tampilan
                holder.statusTextView.visibility = View.VISIBLE
                holder.statusTextView.text = "Status: $status"

                notifyItemChanged(position)
                Toast.makeText(cutiContext, "Status berhasil diperbarui", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(cutiContext, "Gagal memperbarui status", Toast.LENGTH_SHORT).show()
            }
    }
}
