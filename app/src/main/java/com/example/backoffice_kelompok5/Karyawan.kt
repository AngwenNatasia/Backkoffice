package com.example.backoffice_kelompok5

data class Karyawan(
    val nama: String,
    val divisi: String,
    val status: String,
    val jabatan: String,
) {
    constructor() : this("", "", "", "")
}