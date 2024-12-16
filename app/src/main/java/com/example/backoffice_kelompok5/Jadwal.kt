package com.example.backoffice_kelompok5

data class Jadwal(
    val id: String,
    val nama: String,
    val hari: String,
    val jamMasuk: String,
    val jamKeluar: String
){
    constructor() : this("", "", "", "","")
}
