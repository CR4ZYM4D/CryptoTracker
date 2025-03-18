package com.plcoding.cryptotracker.core.presentation.util

import android.content.Context
import com.plcoding.cryptotracker.R
import com.plcoding.cryptotracker.core.domain.util.NetworkError

fun NetworkError.toString(context: Context):String{
    val resId= when(this){
        NetworkError.REQUEST_TIMEOUT -> R.string.REQUEST_TIMEOUT_ERROR
        NetworkError.SERVER_ERROR -> R.string.SERVER_ERROR
        NetworkError.UNKNOWN -> R.string.UNKKNOWN
        NetworkError.SERIALIZATION -> R.string.SERIALIZATION
        NetworkError.NO_INTERNET -> R.string.NO_INTERNET
        NetworkError.TOO_MANY_REQUESTS -> R.string.TOO_MANY_REQUESTS
    }
    return context.getString(resId)
}