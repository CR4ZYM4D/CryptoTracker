package com.plcoding.cryptotracker.crypto.presentation.coin_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.cryptotracker.crypto.domain.Coin
import com.plcoding.cryptotracker.crypto.presentation.models.CoinUi
import com.plcoding.cryptotracker.crypto.presentation.models.toCoinUi
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme

@Composable
fun CoinListItem(coinUI:CoinUi ,
                 onClick:()->Unit,
                 modifier: Modifier = Modifier
    ) {

    Row (modifier = modifier
        .padding(horizontal = 16.dp , vertical = 4.dp)
        .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Icon(
            imageVector = ImageVector.vectorResource(coinUI.iconResource),
            contentDescription = coinUI.symbol,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(85.dp)
        )
        Column{
            Text(
                text = coinUI.symbol,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp
            )
            Text(
                text = coinUI.name,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
        Column (modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End){
            Text(text = "$ ${coinUI.priceUSD.formattedNumber}" ,
                fontSize = 14.sp,
                fontWeight = FontWeight.W200,
                color = MaterialTheme.colorScheme.primary
            )
            PriceChange(change = coinUI.changePercent24Hr,  modifier = Modifier)
        }
    }

}

@PreviewLightDark
@Composable
private fun CoinListScreen(){

    CryptoTrackerTheme {
        CoinListItem(coinUI = previewCoin.toCoinUi() ,
            onClick={} ,
            modifier = Modifier.background(MaterialTheme.colorScheme.background))
    }

}

internal val previewCoin = Coin(

    name = "Bitcoin",
    id = "bitcoin",
    symbol = "BTC",
    rank = 1,
    priceUSD = 100.0,
    changePercent24Hr = 7.0,
    marketCapUSD = 123142351121452.6,

)