package com.example.backoffice_kelompok5

data class Approval(
    val nama: String,
    val jenis: String,
    val alasan: String,
    val status: String
){
    constructor(): this("", "", "", "")
}
