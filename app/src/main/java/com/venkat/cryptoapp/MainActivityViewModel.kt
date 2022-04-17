package com.venkat.cryptoapp

import androidx.lifecycle.ViewModel
import com.venkat.cryptoapp.data.CryptoAPI
import com.venkat.cryptoapp.data.RetrofitInstance
import com.venkat.cryptoapp.model.CryptoCurrency
import com.venkat.cryptoapp.util.AutoRefresh
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class MainActivityViewModel : ViewModel(), AutoRefresh {
    private val cryptoAPI: CryptoAPI = RetrofitInstance.getInstance().create(CryptoAPI::class.java)


    override fun refresh(delay: Long): Flow<Response<CryptoCurrency>> {
        return channelFlow {
            while (true) {
                delay(delay)
                try {
                    send(cryptoAPI.getCurrencies())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }.flowOn(Dispatchers.IO)
    }

}