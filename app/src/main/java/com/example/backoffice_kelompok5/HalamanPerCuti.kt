package com.example.backoffice_kelompok5

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class HalamanPerCuti : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var approvalDatabase: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var cutiList: MutableList<Cuti>
    private lateinit var approvalList: MutableList<Approval>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_izin_cuti)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        cutiList = mutableListOf()
        approvalList = mutableListOf()

        database = FirebaseDatabase.getInstance().getReference("cuti")
        approvalDatabase = FirebaseDatabase.getInstance().getReference("approval")

        loadCutiData()
    }

    private fun loadCutiData() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cutiList.clear()
                for (h in snapshot.children) {
                    val cuti = h.getValue(Cuti::class.java)
                    if (cuti != null) {
                        cutiList.add(cuti)
                    }
                }
                loadApprovalData()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HalamanPerCuti, "Gagal memuat data cuti", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadApprovalData() {
        approvalDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                approvalList.clear()
                for (h in snapshot.children) {
                    val approval = h.getValue(Approval::class.java)
                    if (approval != null) {
                        approvalList.add(approval)
                    }
                }
                recyclerView.adapter = PerCutiAdapter(this@HalamanPerCuti, cutiList, approvalList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HalamanPerCuti, "Gagal memuat data approval", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
