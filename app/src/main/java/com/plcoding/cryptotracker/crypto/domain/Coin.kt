package com.plcoding.cryptotracker.crypto.domain

data class Coin(

    val id:String,
    val name:String,
    val symbol:String,
    val rank:Int,
    val priceUSD: Double? ,
    val changePercent24Hr: Double?,
    val marketCapUSD:Double?,

)
