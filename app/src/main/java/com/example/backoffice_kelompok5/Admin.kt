package com.example.backoffice_kelompok5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.backoffice_kelompok5.Fragment.ManajemenDataFragment
import com.example.backoffice_kelompok5.Fragment.JadwalFragment
import com.example.backoffice_kelompok5.Fragment.PerizinFragment
import com.example.backoffice_kelompok5.Fragment.PercutiFragment
import com.example.backoffice_kelompok5.databinding.ActivityAdminBinding
import com.google.android.material.tabs.TabLayout

class Admin : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tabLayout: TabLayout = binding.tabLayout
        val viewPager: ViewPager = binding.viewPager
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        viewPagerAdapter.tambahFragment(JadwalFragment(), "Jadwal")
        viewPagerAdapter.tambahFragment(ManajemenDataFragment(), "Manajemen Data")

        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    internal class ViewPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(
        fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

        private var fragments: ArrayList<Fragment> = ArrayList<Fragment>()
        private var juduls: ArrayList<String> = ArrayList<String>()

        init {
            fragments = ArrayList()
            juduls = ArrayList()
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        fun tambahFragment(fragment: Fragment, judul: String){
            fragments.add(fragment)
            juduls.add(judul)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return juduls[position]
        }
    }
}
