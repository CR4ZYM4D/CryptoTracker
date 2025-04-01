package com.plcoding.cryptotracker.crypto.presentation.models

import androidx.annotation.DrawableRes
import com.plcoding.cryptotracker.crypto.domain.Coin
import com.plcoding.cryptotracker.core.presentation.util.getDrawableIdForCoin
import com.plcoding.cryptotracker.crypto.domain.CoinPrice
import com.plcoding.cryptotracker.crypto.presentation.coin_detail.DataPoint
import java.text.NumberFormat
import java.util.Locale

data class CoinUi(

    val name:String,
    val rank:Int,
    val symbol:String,
    val id:String,
    val marketCapUSD:DisplayableNumber,
    val priceUSD:DisplayableNumber,
    val changePercent24Hr:DisplayableNumber,
    @DrawableRes val iconResource:Int,
    val history : List<DataPoint> = emptyList()
)

data class DisplayableNumber(

    val number:Double,
    val formattedNumber : String

)

fun Coin.toCoinUi():CoinUi{
    return CoinUi(
        name = name,
        rank = rank,
        symbol = symbol,
        id = id,
        marketCapUSD = marketCapUSD.toDisplayableNumber(),
        priceUSD = priceUSD.toDisplayableNumber(),
        changePercent24Hr = changePercent24Hr.toDisplayableNumber(),
        iconResource = getDrawableIdForCoin(symbol)
    )
}

fun Double?.toDisplayableNumber():DisplayableNumber{

    if(this==null){
        return DisplayableNumber(
            number = 0.0,
            formattedNumber = "-"
        )
    }
        val formatter= NumberFormat.getNumberInstance(Locale.getDefault()).apply {
            minimumFractionDigits=2
            maximumFractionDigits=2
        }
    return DisplayableNumber(
        number = this,
        formattedNumber = formatter.format(this)
    )
}