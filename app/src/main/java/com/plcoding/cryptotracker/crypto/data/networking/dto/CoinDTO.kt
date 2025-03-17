package com.plcoding.cryptotracker.crypto.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class CoinDTO(

    val id:String,
    val rank:Int,
    val name:String,
    val symbol:String,
    val priceUsd:Double,
    val changePercent24Hr:Double?,
    val marketCapUSD:Double

)
