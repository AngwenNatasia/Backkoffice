package com.example.backoffice_kelompok5

data class Presensi(
    val nama: String,
    val tanggal: String,
    val divisi: String,
    val status: String
){
    constructor() : this("", "", "", "")
}
