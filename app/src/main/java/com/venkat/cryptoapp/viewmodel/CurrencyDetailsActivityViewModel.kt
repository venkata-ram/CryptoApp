package com.venkat.cryptoapp.viewmodel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.venkat.cryptoapp.data.CryptoAPI
import com.venkat.cryptoapp.data.RetrofitInstance
import com.venkat.cryptoapp.model.CryptoCurrencyItem
import com.venkat.cryptoapp.util.AutoRefresh
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import java.text.Format
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class CurrencyDetailsActivityViewModel(private val symbol: String) : ViewModel(),
    AutoRefresh<CryptoCurrencyItem>, DefaultLifecycleObserver {
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

    override fun refresh(delay: Long): Flow<Response<CryptoCurrencyItem>> {
        return channelFlow {
            while (flag.value == true) {
                try {
                    send(cryptoAPI.getCurrency(symbol))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                delay(delay)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getDateFromMilliseconds(millis: Long): String {
        val dateFormat = "hh:mm a"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar = Calendar.getInstance()

        calendar.timeInMillis = millis
        return formatter.format(calendar.time)
    }

    fun formatCurrency(currency:String,quoteAsset:String):String{
        var currencyPrice = ""
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("en","in"))
        if(quoteAsset == "inr") {
            currencyPrice = numberFormat.format(currency.toDouble())
        }
        else {
            currencyPrice = currency
        }
        return currencyPrice
    }

    fun formatNumber(number:String) : String{
        val format: Format = NumberFormat.getNumberInstance(Locale("en", "in"))
        return format.format(number.toDouble())
    }
}