package com.example.backoffice_kelompok5

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.example.backoffice_kelompok5.databinding.ActivityMainBinding
import com.example.backoffice_kelompok5.databinding.ActivityMainKaryawanBinding
import com.example.backoffice_kelompok5.CutiKaryawan
import com.example.backoffice_kelompok5.IzinKaryawan
import com.example.backoffice_kelompok5.KehadiranKaryawan
import com.example.backoffice_kelompok5.LemburKaryawan

class MainActivityKaryawan : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainKaryawanBinding
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var cutiFragment: CutiKaryawan
    lateinit var izinFragment: IzinKaryawan
    lateinit var kehadiranFragment: KehadiranKaryawan
    lateinit var lemburFragment: LemburKaryawan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainKaryawanBinding.inflate(layoutInflater)

        setContentView(binding.root)

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.hadir -> {
                kehadiranFragment = KehadiranKaryawan()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, kehadiranFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                Toast.makeText(applicationContext, "Ini Halaman Kehadiran", Toast.LENGTH_SHORT).show()
            }
            R.id.izin -> {
                izinFragment = IzinKaryawan()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, izinFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                Toast.makeText(applicationContext, "Ini Halaman Izin", Toast.LENGTH_SHORT).show()
            }
            R.id.cuti -> {
                cutiFragment = CutiKaryawan()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, cutiFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                Toast.makeText(applicationContext, "Ini Halaman Cuti", Toast.LENGTH_SHORT).show()
            }
            R.id.lembur -> {
                lemburFragment = LemburKaryawan()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, lemburFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                Toast.makeText(applicationContext, "Ini Halaman Lembur", Toast.LENGTH_SHORT).show()
            }

        }
        binding.drawerLayout.closeDrawers()
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)

    }
}