package com.example.backoffice_kelompok5

data class Karyawan(
    val id: String,
    val nama: String,
    val divisi: String,
    val jabatan: String,
    val departemen: String,
    val jenisKelamin: String,
) {
    constructor() : this("", "", "", "", "", "")
}