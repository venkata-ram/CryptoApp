package com.venkat.cryptoapp.data

import com.venkat.cryptoapp.model.CryptoCurrency
import com.venkat.cryptoapp.model.CryptoCurrencyItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CryptoAPI {
    @GET("/sapi/v1/tickers/24hr")
    suspend fun getCurrencies(): Response<CryptoCurrency>

    @GET("/sapi/v1/ticker/24hr")
    suspend fun getCurrency(@Query("symbol") symbol : String) : Response<CryptoCurrencyItem>
}