package com.example.backoffice_kelompok5

data class Lembur(
    val nama: String,
    val lama:Int,
    val tanggal: String,
    val divisi: String,
){
    constructor() : this("", 0, "", "")
}
