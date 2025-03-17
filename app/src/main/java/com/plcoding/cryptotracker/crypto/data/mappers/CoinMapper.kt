package com.plcoding.cryptotracker.crypto.data.mappers

import com.plcoding.cryptotracker.crypto.data.networking.dto.CoinDTO
import com.plcoding.cryptotracker.crypto.domain.Coin

fun CoinDTO.toCoin(): Coin{

    return Coin(

        id = id,
        name = name,
        symbol = symbol,
        rank = rank,
        priceUSD = priceUsd,
        changePercent24Hr = changePercent24Hr,
        marketCapUSD = marketCapUSD,
    )

}