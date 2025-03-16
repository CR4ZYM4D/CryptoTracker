package com.plcoding.cryptotracker.crypto.presentation.coin_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.plcoding.cryptotracker.crypto.presentation.coin_list.components.CoinListItem
import com.plcoding.cryptotracker.crypto.presentation.coin_list.components.CoinListState
import com.plcoding.cryptotracker.crypto.presentation.coin_list.components.previewCoin
import com.plcoding.cryptotracker.crypto.presentation.models.toCoinUi
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme

@Composable
fun CoinListScreen(state: CoinListState, modifier:Modifier = Modifier){

    if(state.isLoading){
        Box(modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
    }
    else{
        LazyColumn(modifier = modifier
            .fillMaxSize()
        ) {
            items(state.coinList){coinUI->
                CoinListItem(coinUI = coinUI , onClick={} , modifier = modifier.fillMaxWidth())
            }
        }
    }

}


@PreviewLightDark
@Composable
private fun coinListScreen(){

    CryptoTrackerTheme {
        CoinListScreen(state = CoinListState(
            coinList = (1..50).map { previewCoin.toCoinUi().copy(id = it.toString()) }),
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }

}