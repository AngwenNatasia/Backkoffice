package com.example.backoffice_kelompok5

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor

class MainActivityKaryawan : AppCompatActivity() {
    private lateinit var tvWelcome: TextView
    private lateinit var tvSchedule: TextView
    private lateinit var btnHadir: Button
    private lateinit var btnBiometrik: Button
    private lateinit var btnLembur: Button
    private lateinit var btnLogout: Button
    private lateinit var btnCuti: Button
    private lateinit var btnIzin: Button
    private lateinit var database: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var biometricExecutor: Executor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_karyawan)

        // Initialize views
        tvWelcome = findViewById(R.id.tv_welcome)
        tvSchedule = findViewById(R.id.tv_schedule)
        btnHadir = findViewById(R.id.btn_hadir)
        btnBiometrik = findViewById(R.id.btn_biometrik)
        btnLembur = findViewById(R.id.btn_lembur)
        btnCuti = findViewById(R.id.btn_cuti)
        btnIzin = findViewById(R.id.btn_izin)
        btnLogout = findViewById(R.id.btn_logout)

        sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        database = FirebaseDatabase.getInstance().reference

        biometricExecutor = ContextCompat.getMainExecutor(this)
        biometricPrompt = createBiometricPrompt()


        displayUserName()
        displayUserSchedule()


        btnHadir.setOnClickListener {
            recordAttendance()
        }


        btnBiometrik.setOnClickListener {
            biometricPrompt.authenticate(createBiometricPromptInfo())
        }

        btnCuti.setOnClickListener {
            val intent = Intent(this, CutiKaryawan::class.java)
            startActivity(intent)
        }

        btnIzin.setOnClickListener {
            val intent = Intent(this, IzinKaryawan::class.java)
            startActivity(intent)
        }


        btnLembur.setOnClickListener {
            val intent = Intent(this@MainActivityKaryawan, LemburKaryawan::class.java)
            startActivity(intent)
        }


        btnLogout.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {

        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()


        Toast.makeText(this, "Anda telah logout", Toast.LENGTH_SHORT).show()


        val intent = Intent(this@MainActivityKaryawan, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun displayUserName() {
        val userId = sharedPreferences.getString("userId", null)
        if (userId.isNullOrEmpty()) {
            tvWelcome.text = "Selamat Datang, User"
            return
        }

        database.child("auth").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val nama = snapshot.child("nama").getValue(String::class.java) ?: "User"
                    tvWelcome.text = "Selamat Datang, $nama"
                } else {
                    tvWelcome.text = "Selamat Datang, User"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                tvWelcome.text = "Selamat Datang, User"
                Toast.makeText(this@MainActivityKaryawan, "Gagal memuat data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayUserSchedule() {
        val userId = sharedPreferences.getString("userId", null)
        if (userId.isNullOrEmpty()) {
            tvSchedule.text = "Schedule: Tidak tersedia"
            return
        }

        database.child("auth").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val nama = snapshot.child("nama").getValue(String::class.java) ?: "User"
                    database.child("jadwal").orderByChild("nama").equalTo(nama)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(jadwalSnapshot: DataSnapshot) {
                                if (jadwalSnapshot.exists()) {
                                    val jadwal = jadwalSnapshot.children.firstOrNull()?.getValue(Jadwal::class.java)
                                    tvSchedule.text = "Jadwal: ${jadwal?.hari}, ${jadwal?.jamMasuk} - ${jadwal?.jamKeluar}"
                                } else {
                                    tvSchedule.text = "Schedule: Tidak tersedia"
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                tvSchedule.text = "Schedule: Tidak tersedia"
                                Toast.makeText(this@MainActivityKaryawan, "Gagal memuat jadwal: ${error.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                } else {
                    tvSchedule.text = "Schedule: Tidak tersedia"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                tvSchedule.text = "Schedule: Tidak tersedia"
                Toast.makeText(this@MainActivityKaryawan, "Gagal memuat data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun recordAttendance() {
        val userId = sharedPreferences.getString("userId", null)
        if (userId.isNullOrEmpty()) {
            Toast.makeText(this, "User ID tidak ditemukan!", Toast.LENGTH_SHORT).show()
            return
        }

        database.child("auth").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val nama = snapshot.child("nama").getValue(String::class.java) ?: "User"
                    val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

                    // Save to presensi table
                    val presensiData = mapOf(
                        "nama" to nama,
                        "timestamp" to timestamp
                    )

                    database.child("presensi").push().setValue(presensiData)
                        .addOnSuccessListener {
                            Toast.makeText(this@MainActivityKaryawan, "Presensi berhasil dicatat!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@MainActivityKaryawan, "Gagal mencatat presensi!", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this@MainActivityKaryawan, "Data pengguna tidak ditemukan!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivityKaryawan, "Gagal memuat data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createBiometricPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("Verifikasi Biometrik")
            .setSubtitle("Gunakan sidik jari Anda untuk mencatat presensi")
            .setNegativeButtonText("Batal")
            .build()
    }

    private fun createBiometricPrompt(): BiometricPrompt {
        return BiometricPrompt(this, biometricExecutor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                recordAttendance()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(this@MainActivityKaryawan, "Autentikasi gagal: $errString", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(this@MainActivityKaryawan, "Autentikasi tidak berhasil. Coba lagi.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
