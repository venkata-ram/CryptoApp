package com.venkat.cryptoapp.util

import android.app.DownloadManager
import com.venkat.cryptoapp.model.CryptoCurrency
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface AutoRefresh<T> {
    fun refresh(delay:Long) : Flow<Response<T>>
}