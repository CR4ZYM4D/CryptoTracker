package com.plcoding.cryptotracker.core.domain.util

enum class NetworkError:Error {

    REQUEST_TIMEOUT,
    SERVER_ERROR,
    UNKNOWN,
    SERIALIZATION,
    NO_INTERNET,
    TOO_MANY_REQUESTS

}