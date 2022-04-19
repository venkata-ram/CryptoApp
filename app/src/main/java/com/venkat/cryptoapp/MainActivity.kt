package com.venkat.cryptoapp

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.venkat.cryptoapp.adapter.CurrencyRecyclerviewAdapter
import com.venkat.cryptoapp.model.CryptoCurrencyItem
import com.venkat.cryptoapp.viewmodel.MainActivityViewModel
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter : CurrencyRecyclerviewAdapter
    private lateinit var progressBar: ProgressBar

    private lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        lifecycle.addObserver(viewModel)
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progress_bar)
        initRecyclerView()
        val swipeToRefreshLayout : SwipeRefreshLayout = findViewById( R.id.swipe_refresh_layout)
        swipeToRefreshLayout.setOnRefreshListener {
            swipeToRefreshLayout.isRefreshing = false
            if(isOnline(this))
                displayCurrenciesList()
        }
    }

    override fun onResume() {
        super.onResume()
        if(isOnline(this)){
            displayCurrenciesList()
        }
    }

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CurrencyRecyclerviewAdapter{currency:CryptoCurrencyItem ->
            currencyItemClicked(currency)
        }
        recyclerView.adapter = adapter

    }

    private fun displayCurrenciesList() {
        val data = viewModel.refresh(1000)
        CoroutineScope(Dispatchers.IO).launch {
            data.collect{ response ->
                val responses = response.body()
                responses?.let {
                    withContext(Dispatchers.Main){
                        adapter.setList(it)
                        progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun currencyItemClicked(currency : CryptoCurrencyItem){
        val intent = Intent(this,CurrencyDetailsActivity::class.java)
        intent.putExtra("currency_symbol",currency.symbol)
        startActivity(intent)
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

    /*override fun onResume() {
        super.onResume()
        viewModel.flag.value = true
    }

    override fun onPause() {
        super.onPause()
        viewModel.flag.value = false
    }*/




}