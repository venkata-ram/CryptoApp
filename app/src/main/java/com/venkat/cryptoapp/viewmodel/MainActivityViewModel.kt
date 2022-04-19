package com.venkat.cryptoapp.viewmodel

import androidx.lifecycle.*
import com.venkat.cryptoapp.data.CryptoAPI
import com.venkat.cryptoapp.data.RetrofitInstance
import com.venkat.cryptoapp.model.CryptoCurrency
import com.venkat.cryptoapp.util.AutoRefresh
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MainActivityViewModel : ViewModel(), AutoRefresh<CryptoCurrency>, DefaultLifecycleObserver {
    private val cryptoAPI: CryptoAPI = RetrofitInstance.getInstance().create(CryptoAPI::class.java)

    private var flag = MutableLiveData<Boolean>()

    init {
        flag.value = true
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        flag.value = true
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        flag.value = false
    }

    override fun refresh(delay: Long): Flow<Response<CryptoCurrency>> {
        return channelFlow {
            while (flag.value == true) {
                try {
                    send(cryptoAPI.getCurrencies())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                delay(delay)
            }
        }.flowOn(Dispatchers.IO)
    }


}