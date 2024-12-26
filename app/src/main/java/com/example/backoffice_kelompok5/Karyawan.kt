package com.example.backoffice_kelompok5

import java.util.Date

data class Karyawan(
    val id: String,
    val nama: String,
    val divisi: String,
    val jabatan: String,
    val departemen: String,
    val jenisKelamin: String,
    val noHP: String,
    val tglGabung: Date
) {
    constructor() : this("", "", "", "", "", "", "", Date())
}