package com.plcoding.cryptotracker.crypto.presentation.coin_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.cryptotracker.crypto.presentation.models.DisplayableNumber
import com.plcoding.cryptotracker.crypto.presentation.models.toCoinUi
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme
import com.plcoding.cryptotracker.ui.theme.greenBackground

@Composable
fun PriceChange(change:DisplayableNumber , modifier: Modifier = Modifier){

    val color = if(change.number >0.0) Color.Green
    else MaterialTheme.colorScheme.onErrorContainer

    val bgColor = if(change.number >0.0) greenBackground
    else MaterialTheme.colorScheme.errorContainer
    Row (modifier = Modifier
        .clip(RoundedCornerShape(100))
        .background(color = bgColor)
        .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(imageVector =
            if(change.number>0.0) Icons.Default.KeyboardArrowUp
            else Icons.Default.KeyboardArrowDown ,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
        Text(text = "${change.formattedNumber} %",
            fontWeight = FontWeight.Medium,
            color = color,
            fontSize = 12.sp
        )
    }

}

@PreviewLightDark
@Composable
private fun PreviewPriceChange(change: DisplayableNumber = previewCoin.toCoinUi().changePercent24Hr){

    CryptoTrackerTheme {
        PriceChange(change = change , modifier = Modifier.background(MaterialTheme.colorScheme.background))

    }

}