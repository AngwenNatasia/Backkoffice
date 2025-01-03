package com.example.backoffice_kelompok5

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class HalamanPerIzin : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var approvalDatabase: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var izinList: MutableList<Izin>
    private lateinit var approvalList: MutableList<Approval>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_izin_cuti)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        izinList = mutableListOf()
        approvalList = mutableListOf()
        database = FirebaseDatabase.getInstance().getReference("izin")
        approvalDatabase = FirebaseDatabase.getInstance().getReference("approval")

        loadIzinData()
    }

    private fun loadIzinData() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                izinList.clear()
                for (h in snapshot.children) {
                    val izin = h.getValue(Izin::class.java)
                    if (izin != null) {
                        izinList.add(izin)
                    }
                }
                loadApprovalData()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HalamanPerIzin, "Gagal memuat data izin", Toast.LENGTH_SHORT).show()
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
                recyclerView.adapter = PerIzinAdapter(this@HalamanPerIzin, izinList, approvalList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HalamanPerIzin, "Gagal memuat data approval", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
