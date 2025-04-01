package com.plcoding.cryptotracker.crypto.data.mappers

import android.provider.Settings
import com.plcoding.cryptotracker.crypto.data.networking.dto.CoinDTO
import com.plcoding.cryptotracker.crypto.data.networking.dto.CoinPriceDto
import com.plcoding.cryptotracker.crypto.domain.Coin
import com.plcoding.cryptotracker.crypto.domain.CoinPrice
import java.time.Instant
import java.time.ZoneId

fun CoinDTO.toCoin(): Coin{

    return Coin(

        id = id,
        name = name,
        symbol = symbol,
        rank = rank,
        priceUSD = priceUsd,
        changePercent24Hr = changePercent24Hr,
        marketCapUSD = marketCapUsd,
    )

}

fun CoinPriceDto.toCoinPrice():CoinPrice{
    return CoinPrice(
        priceUsd = priceUsd,
        time = Instant
            .ofEpochMilli(time)
            .atZone(ZoneId.systemDefault())
    )
}