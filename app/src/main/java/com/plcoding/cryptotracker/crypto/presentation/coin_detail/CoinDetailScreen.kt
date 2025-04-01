package com.plcoding.cryptotracker.crypto.presentation.coin_detail

import android.provider.ContactsContract.Data
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.cryptotracker.R
import com.plcoding.cryptotracker.crypto.presentation.coin_list.components.CoinListState
import com.plcoding.cryptotracker.crypto.presentation.coin_list.components.previewCoin
import com.plcoding.cryptotracker.crypto.presentation.models.toCoinUi
import com.plcoding.cryptotracker.crypto.presentation.models.toDisplayableNumber
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme
import com.plcoding.cryptotracker.ui.theme.greenBackground

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CoinDetailScreen( state:CoinListState,
    modifier: Modifier = Modifier) {

    if(state.isLoading){
        Box(modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
    }

    else if(state.selectedCoin !=null){

        val coin = state.selectedCoin
        Column (modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(coin.iconResource),
                contentDescription = coin.symbol,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(150.dp)
                    .padding(15.dp)
            )
            Text(
                text = coin.name,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 40.sp
            )
            Text(
                text = coin.symbol,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            FlowRow (modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center){
                CardDetail(
                    title = stringResource(R.string.market_cap),
                    formattedText = "$ ${coin.marketCapUSD.formattedNumber}",
                    icon = ImageVector.vectorResource(R.drawable.stock)
                )
                Row {
                    CardDetail(
                        title = stringResource(R.string.price),
                        formattedText = "$ ${coin.priceUSD.formattedNumber}",
                        icon = ImageVector.vectorResource(R.drawable.dollar)
                    )

                    val change =
                        (coin.changePercent24Hr.number * coin.priceUSD.number / 100.0).toDisplayableNumber()
                    val isPositive = change.number>0
                    val contentColor= if(isPositive){if(isSystemInDarkTheme()) Color.Green else greenBackground}
                    else{MaterialTheme.colorScheme.error}
                    CardDetail(
                        title = stringResource(R.string.change_24hr),
                        formattedText = "$ ${change.formattedNumber}",
                        icon = if(isPositive)
                            ImageVector.vectorResource(R.drawable.trending)
                        else
                            ImageVector.vectorResource(R.drawable.trending_down),
                        contentColor = contentColor
                    )
                }
            }

            AnimatedVisibility(visible = coin.history.isNotEmpty()) {

                var selectedDataPoint by remember {
                    mutableStateOf<DataPoint?>(null)
                }

                var labelWidth by remember {
                    mutableFloatStateOf(0f)
                }

                var chartWidth by remember {
                    mutableFloatStateOf(0f)
                }

                val numVisiblePoints = if(labelWidth>0){
                    ((chartWidth -2.5f*labelWidth)/labelWidth).toInt()
                }else 0

                val startIndex = (coin.history.lastIndex - numVisiblePoints).coerceAtLeast(0)

                LineChart(
                    dataPoints = coin.history,
                    chartStyle = ChartStyle(
                        chartLineColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.secondary,
                        selectedColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        gridLineThickness = 2f,
                        axisLineThickness = 2f,
                        labelFontSize = 14.sp,
                        minYLabelSpacing = 12.dp,
                        verticalPadding = 10.dp,
                        horizontalPadding = 10.dp,
                        xLabelSpacing = 8.dp
                    ),
                    visiblePointIndices = startIndex .. coin.history.lastIndex,
                    unit = "$",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16 / 9f)
                        .onSizeChanged { chartWidth= it.width.toFloat() }
                    ,
                    selectedDataPoint = selectedDataPoint,
                    onSelectedDataPoint = { selectedDataPoint = it },
                    onXLabelWidthChange = { labelWidth = it }
                )
            }

        }

    }

}

@PreviewLightDark
@Composable
fun pCoinDetailScreen(){
    CryptoTrackerTheme {
        CoinDetailScreen(modifier = Modifier.background(MaterialTheme.colorScheme.background) ,
            state = CoinListState(selectedCoin = previewCoin.toCoinUi()))
    }
}