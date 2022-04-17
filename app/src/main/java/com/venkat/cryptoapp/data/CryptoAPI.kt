package com.venkat.cryptoapp.data

import com.venkat.cryptoapp.model.CryptoCurrency
import retrofit2.Response
import retrofit2.http.GET

interface CryptoAPI {
    @GET("/sapi/v1/tickers/24hr")
    suspend fun getCurrencies(): Response<CryptoCurrency>
}