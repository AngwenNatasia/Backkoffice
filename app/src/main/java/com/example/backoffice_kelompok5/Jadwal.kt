package com.example.backoffice_kelompok5

data class Jadwal(
    val nama: String,
    val hari: String,
    val jamMasuk: String,
    val jamKeluar: String,
    val status: String
){
    constructor() : this("", "", "", "", "")
}
