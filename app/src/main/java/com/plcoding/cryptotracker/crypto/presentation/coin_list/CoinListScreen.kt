package com.plcoding.cryptotracker.crypto.presentation.coin_list

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.plcoding.cryptotracker.core.presentation.util.toString
import com.plcoding.cryptotracker.crypto.presentation.coin_detail.CoinDetailScreen
import com.plcoding.cryptotracker.crypto.presentation.coin_list.components.CoinListItem
import com.plcoding.cryptotracker.crypto.presentation.coin_list.components.CoinListState
import com.plcoding.cryptotracker.crypto.presentation.coin_list.components.previewCoin
import com.plcoding.cryptotracker.crypto.presentation.models.toCoinUi
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext

@Composable
fun CoinListScreen(state: CoinListState,
                   event: Flow<CoinListEvent>,
                   modifier:Modifier = Modifier){

    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current
    LaunchedEffect(lifecycle.lifecycle) {

        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
            withContext(Dispatchers.Main.immediate){
                event.collect{event->
                    when(event){
                        is CoinListEvent.Error -> Toast.makeText(context ,
                            event.error.toString(context) , Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }

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
                CoinListItem(coinUI = coinUI ,
                    onClick={} ,
                    modifier = modifier.fillMaxWidth())
            }
        }
    }

}


@PreviewLightDark
@Composable
private fun PreviewCoinListScreen(){

    CryptoTrackerTheme {
        CoinListScreen(state = CoinListState(
            coinList = (1..50).map { previewCoin.toCoinUi().copy(id = it.toString()) }),
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            event = emptyFlow()
        )
    }

}