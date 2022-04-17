package com.venkat.cryptoapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.venkat.cryptoapp.adapter.CurrencyRecyclerviewAdapter
import com.venkat.cryptoapp.data.CryptoAPI
import com.venkat.cryptoapp.data.RetrofitInstance
import com.venkat.cryptoapp.model.CryptoCurrency
import com.venkat.cryptoapp.model.CryptoCurrencyItem
import com.venkat.cryptoapp.util.AutoRefresh
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import retrofit2.Response
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter : CurrencyRecyclerviewAdapter

    private lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CurrencyRecyclerviewAdapter{currency:CryptoCurrencyItem ->
            currencyItemClicked(currency)
        }
        recyclerView.adapter = adapter
        if(isOnline(this)){
            displayCurrenciesList()
        }

    }

    private fun displayCurrenciesList() {
        val data = viewModel.refresh(1000)
        CoroutineScope(Dispatchers.IO).launch {
            data.collect{ response ->
                val responses = response.body()
                responses?.let {
                    withContext(Dispatchers.Main){
                        adapter.setList(it)
                    }
                }
            }
        }
    }

    private fun currencyItemClicked(currency : CryptoCurrencyItem){
        Log.i("MyTag",currency.symbol)
    }

    // checking network connectivity
    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }




}